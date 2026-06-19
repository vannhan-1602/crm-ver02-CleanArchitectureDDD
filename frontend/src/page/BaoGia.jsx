import { useEffect, useMemo, useState } from 'react'
import './BaoGia.css'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8081'

const emptyLine = {
  sanPhamId: '',
  soLuong: 1,
  donGia: '',
}

const emptyForm = {
  maBaoGia: '',
  khachHangId: '',
  nhanVienId: '',
  trangThai: 'Nhap',
  chiTiets: [emptyLine],
}

function formatCurrency(value) {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
    maximumFractionDigits: 0,
  }).format(Number(value || 0))
}

function formatDateTime(value) {
  if (!value) return '-'
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return value
  return new Intl.DateTimeFormat('vi-VN', {
    dateStyle: 'short',
    timeStyle: 'short',
  }).format(date)
}

function BaoGia() {
  const [items, setItems] = useState([])
  const [products, setProducts] = useState([])
  const [khachHangList, setKhachHangList] = useState([])
  const [nhanVienList, setNhanVienList] = useState([])
  const [form, setForm] = useState(emptyForm)
  const [editingId, setEditingId] = useState(null)
  const [loading, setLoading] = useState(false)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [search, setSearch] = useState('')

  const productMap = useMemo(() => {
    return new Map(products.map((product) => [String(product.sanPhamId ?? product.id), product]))
  }, [products])

  const khachHangMap = useMemo(
    () =>
      new Map(
        khachHangList.map((item) => [String(item.id), item.tenKhachHang ?? `KH #${item.id}`]),
      ),
    [khachHangList],
  )

  const nhanVienMap = useMemo(
    () =>
      new Map(
        nhanVienList.map((item) => [
          String(item.id),
          item.hoTen ?? item.tenNhanVien ?? `NV #${item.id}`,
        ]),
      ),
    [nhanVienList],
  )

  const findKhachHangIdByName = (tenKhachHang) => {
    if (!tenKhachHang) return ''
    const match = khachHangList.find((item) => item.tenKhachHang === tenKhachHang)
    return match?.id ?? ''
  }

  const findNhanVienIdByName = (tenNhanVien) => {
    if (!tenNhanVien) return ''
    const match = nhanVienList.find(
      (item) => (item.hoTen ?? item.tenNhanVien) === tenNhanVien,
    )
    return match?.id ?? ''
  }

  const enrichedItems = useMemo(() => {
    return items.map((item) => {
      const chiTiets = (item.chiTiets || []).map((line) => {
        const product = productMap.get(String(line.sanPhamId))
        return {
          ...line,
          tenSanPham: product?.tenSanPham ?? product?.tenSP ?? `SP #${line.sanPhamId}`,
          maSanPham: product?.maSanPham ?? product?.maSP ?? '-',
        }
      })
      return { ...item, chiTiets }
    })
  }, [items, productMap])

  const filteredItems = useMemo(() => {
    const keyword = search.trim().toLowerCase()
    if (!keyword) return enrichedItems
    return enrichedItems.filter((item) =>
      [
        item.maBaoGia,
        item.tenKhachHang,
        item.tenNhanVien,
        khachHangMap.get(String(item.khachHangId)),
        nhanVienMap.get(String(item.nhanVienId)),
        item.trangThai,
        item.tongTien,
        ...(item.chiTiets || []).flatMap((line) => [
          line.maSanPham,
          line.tenSanPham,
          line.sanPhamId,
        ]),
      ]
        .filter(Boolean)
        .some((value) => String(value).toLowerCase().includes(keyword)),
    )
  }, [enrichedItems, search, khachHangMap, nhanVienMap])

  const loadProducts = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/sanpham`)
      if (!response.ok) {
        throw new Error(`Khong the tai san pham (${response.status})`)
      }
      const data = await response.json()
      setProducts(Array.isArray(data) ? data : [])
    } catch (err) {
      setError(err.message || 'Tai danh sach san pham that bai')
    }
  }

  const loadKhachHang = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/khach-hang`)
      if (!response.ok) return
      const data = await response.json()
      setKhachHangList(Array.isArray(data) ? data : [])
    } catch {
      // Dropdown khách hàng chỉ là dữ liệu hỗ trợ.
    }
  }

  const loadNhanVien = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/nhan-vien`)
      if (!response.ok) return
      const data = await response.json()
      setNhanVienList(Array.isArray(data) ? data : [])
    } catch {
      // Dropdown nhân viên chỉ là dữ liệu hỗ trợ.
    }
  }

  const loadBaoGia = async () => {
    setLoading(true)
    setError('')
    try {
      const response = await fetch(`${API_BASE_URL}/api/bao-gia`, {
        cache: 'no-store',
      })
      if (!response.ok) {
        throw new Error(`Khong the tai danh sach bao gia (${response.status})`)
      }
      const data = await response.json()
      setItems(Array.isArray(data) ? data : [])
    } catch (err) {
      setError(err.message || 'Tai danh sach bao gia that bai')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    const initialize = async () => {
      await Promise.all([loadProducts(), loadKhachHang(), loadNhanVien(), loadBaoGia()])
    }

    void initialize()
  }, [])

  const resetForm = () => {
    setEditingId(null)
    setForm(emptyForm)
  }

  const handleFieldChange = (event) => {
    const { name, value } = event.target
    setForm((current) => ({ ...current, [name]: value }))
  }

  const handleLineChange = (index, field, value) => {
    setForm((current) => ({
      ...current,
      chiTiets: current.chiTiets.map((line, lineIndex) => {
        if (lineIndex !== index) return line
        const nextLine = { ...line, [field]: value }
        if (field === 'sanPhamId') {
          const product = productMap.get(String(value))
          nextLine.donGia = product?.giaBan ?? ''
        }
        return nextLine
      }),
    }))
  }

  const addLine = () => {
    setForm((current) => ({
      ...current,
      chiTiets: [...current.chiTiets, { ...emptyLine }],
    }))
  }

  const removeLine = (index) => {
    setForm((current) => {
      const next = current.chiTiets.filter((_, lineIndex) => lineIndex !== index)
      return {
        ...current,
        chiTiets: next.length > 0 ? next : [{ ...emptyLine }],
      }
    })
  }

  const validateForm = () => {
    if (!String(form.maBaoGia ?? '').trim()) return 'Ma bao gia khong duoc rong'
    if (!String(form.khachHangId ?? '').trim()) return 'Khach hang khong duoc rong'
    const validLines = form.chiTiets.filter((line) => line.sanPhamId)
    if (validLines.length === 0) return 'Bao gia phai co it nhat 1 san pham'
    for (const line of validLines) {
      if (!line.sanPhamId) return 'Vui long chon san pham'
      if (!Number(line.soLuong) || Number(line.soLuong) <= 0) return 'So luong phai lon hon 0'
    }
    return ''
  }

  const normalizedLines = useMemo(() => {
    return form.chiTiets
      .filter((line) => line.sanPhamId)
      .map((line) => ({
        sanPhamId: Number(line.sanPhamId),
        soLuong: Number(line.soLuong),
        donGia: line.donGia === '' ? null : Number(line.donGia),
      }))
  }, [form.chiTiets])

  const totalDraft = useMemo(() => {
    return normalizedLines.reduce((sum, line) => sum + line.soLuong * (line.donGia || 0), 0)
  }, [normalizedLines])

  const handleSubmit = async (event) => {
    event.preventDefault()
    const validationMessage = validateForm()
    if (validationMessage) {
      setError(validationMessage)
      setSuccess('')
      return
    }

    setSaving(true)
    setError('')
    setSuccess('')

    const payload = {
      maBaoGia: String(form.maBaoGia ?? '').trim(),
      khachHangId: Number(String(form.khachHangId)),
      nhanVienId: String(form.nhanVienId ?? '') ? Number(String(form.nhanVienId)) : null,
      trangThai: form.trangThai,
      chiTiets: normalizedLines,
    }

    try {
      const response = await fetch(
        editingId ? `${API_BASE_URL}/api/bao-gia/${editingId}` : `${API_BASE_URL}/api/bao-gia`,
        {
          method: editingId ? 'PUT' : 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(payload),
        },
      )

      if (!response.ok) {
        throw new Error(
          editingId
            ? `Cap nhat that bai (${response.status})`
            : `Tao moi that bai (${response.status})`,
        )
      }

      const savedItem = await response.json()
      const selectedKhachHang = khachHangMap.get(String(form.khachHangId))
      const selectedNhanVien = String(form.nhanVienId ?? '')
        ? nhanVienMap.get(String(form.nhanVienId))
        : null
      const updatedItem = {
        ...savedItem,
        maBaoGia: String(form.maBaoGia ?? '').trim(),
        khachHangId: Number(String(form.khachHangId)),
        nhanVienId: String(form.nhanVienId ?? '') ? Number(String(form.nhanVienId)) : null,
        tenKhachHang: selectedKhachHang ?? savedItem.tenKhachHang,
        tenNhanVien: selectedNhanVien ?? savedItem.tenNhanVien,
        trangThai: form.trangThai,
        chiTiets: normalizedLines.map((line) => ({ ...line })),
        tongTien: totalDraft,
      }

      await loadBaoGia()
      setItems((current) =>
        current.map((item) =>
          String(item.id) === String(updatedItem.id) ? updatedItem : item,
        ),
      )
      resetForm()
      setSuccess(editingId ? 'Cap nhat bao gia thanh cong' : 'Tao bao gia thanh cong')
    } catch (err) {
      setError(err.message || 'Khong the luu bao gia')
    } finally {
      setSaving(false)
    }
  }

  const handleEdit = (item) => {
    setEditingId(item.id)
    setForm({
      maBaoGia: item.maBaoGia ?? '',
      khachHangId: String(
        item.khachHangId ?? findKhachHangIdByName(item.tenKhachHang) ?? '',
      ),
      nhanVienId: String(
        item.nhanVienId ?? findNhanVienIdByName(item.tenNhanVien) ?? '',
      ),
      trangThai: item.trangThai ?? 'Nhap',
      chiTiets:
        item.chiTiets?.length > 0
          ? item.chiTiets.map((line) => ({
              sanPhamId: line.sanPhamId ?? '',
              soLuong: line.soLuong ?? 1,
              donGia: line.donGia ?? '',
            }))
          : [{ ...emptyLine }],
    })
    setError('')
    setSuccess('')
  }

  const handleDelete = async (id) => {
    const confirmed = window.confirm('Ban co muon xoa bao gia nay khong?')
    if (!confirmed) return

    setError('')
    setSuccess('')

    try {
      const response = await fetch(`${API_BASE_URL}/api/bao-gia/${id}`, {
        method: 'DELETE',
      })
      if (!response.ok && response.status !== 204) {
        throw new Error(`Xoa that bai (${response.status})`)
      }
      await loadBaoGia()
      if (editingId === id) resetForm()
      setSuccess('Xoa bao gia thanh cong')
    } catch (err) {
      setError(err.message || 'Khong the xoa bao gia')
    }
  }

  const stats = useMemo(() => {
    return {
      total: items.length,
      drafts: items.filter((item) => item.trangThai === 'Nhap').length,
      sent: items.filter((item) => item.trangThai === 'DaGui').length,
      accepted: items.filter((item) => item.trangThai === 'ChapNhan').length,
      rejected: items.filter((item) => item.trangThai === 'TuChoi').length,
    }
  }, [items])

  return (
    <main className="baogia-page">
      <section className="baogia-hero">
        <div>
          <p className="eyebrow">CRM / Bao gia</p>
          <h1>Quan ly bao gia</h1>
          <p className="subtitle">
            Tao bao gia co nhieu dong san pham, tu tinh tong tien va luu qua API backend.
          </p>
        </div>

        <div className="toolbar">
          <input
            className="search"
            type="search"
            placeholder="Tim theo ma, khach hang, san pham, trang thai..."
            value={search}
            onChange={(event) => setSearch(event.target.value)}
          />
          <button className="secondary-btn" type="button" onClick={loadBaoGia}>
            Tai lai
          </button>
        </div>
      </section>

      <section className="stats-row">
        <article className="stat-card">
          <span>Tong so</span>
          <strong>{stats.total}</strong>
        </article>
        <article className="stat-card">
          <span>Nhap</span>
          <strong>{stats.drafts}</strong>
        </article>
        <article className="stat-card">
          <span>Da gui</span>
          <strong>{stats.sent}</strong>
        </article>
        <article className="stat-card">
          <span>Chap nhan</span>
          <strong>{stats.accepted}</strong>
        </article>
        <article className="stat-card">
          <span>Tu choi</span>
          <strong>{stats.rejected}</strong>
        </article>
      </section>

      <section className="content-grid">
        <form className="panel form-panel" onSubmit={handleSubmit}>
          <div className="panel-head">
            <div>
              <h2>{editingId ? 'Cap nhat bao gia' : 'Tao bao gia moi'}</h2>
              <p>Du lieu se goi vao API `api/bao-gia`.</p>
            </div>
            {editingId ? (
              <button className="ghost-btn" type="button" onClick={resetForm}>
                Huy sua
              </button>
            ) : null}
          </div>

          <label>
            Ma bao gia
            <input
              name="maBaoGia"
              value={form.maBaoGia}
              onChange={handleFieldChange}
              placeholder="BG-0001"
            />
          </label>

          <div className="two-col">
            <label>
              Khach hang
              <select
                name="khachHangId"
                value={form.khachHangId}
                onChange={handleFieldChange}
              >
                <option value="">Chon khach hang</option>
                {khachHangList.map((item) => (
                  <option key={item.id} value={item.id}>
                    {item.tenKhachHang ?? `KH #${item.id}`}
                  </option>
                ))}
              </select>
            </label>

            <label>
              Nhan vien
              <select
                name="nhanVienId"
                value={form.nhanVienId}
                onChange={handleFieldChange}
              >
                <option value="">Chon nhan vien</option>
                {nhanVienList.map((item) => (
                  <option key={item.id} value={item.id}>
                    {item.hoTen ?? item.tenNhanVien ?? `NV #${item.id}`}
                  </option>
                ))}
              </select>
            </label>
          </div>

          <label>
            Trang thai
            <select name="trangThai" value={form.trangThai} onChange={handleFieldChange}>
              <option value="Nhap">Nhap</option>
              <option value="DaGui">Da gui</option>
              <option value="TuChoi">Tu choi</option>
              <option value="ChapNhan">Chap nhan</option>
            </select>
          </label>

          <div className="detail-header">
            <div>
              <h3>Chi tiet san pham</h3>
              <p>Chon san pham tu danh sach, so luong va don gia se tinh ngay ben duoi.</p>
            </div>
            <button type="button" className="ghost-btn" onClick={addLine}>
              + Them dong
            </button>
          </div>

          <div className="detail-list">
            {form.chiTiets.map((line, index) => (
              <div className="detail-row" key={`${index}-${line.sanPhamId}`}>
                <label>
                  San pham
                  <select
                    value={line.sanPhamId}
                    onChange={(event) => handleLineChange(index, 'sanPhamId', event.target.value)}
                  >
                    <option value="">Chon san pham</option>
                    {products.map((product) => (
                      <option
                        key={product.sanPhamId ?? product.id}
                        value={product.sanPhamId ?? product.id}
                      >
                        {(product.maSanPham ?? product.maSP) +
                          ' - ' +
                          (product.tenSanPham ?? product.tenSP)}
                      </option>
                    ))}
                  </select>
                </label>

                <label>
                  So luong
                  <input
                    type="number"
                    min="1"
                    value={line.soLuong}
                    onChange={(event) => handleLineChange(index, 'soLuong', event.target.value)}
                  />
                </label>

                <label>
                  Don gia
                  <input
                    type="number"
                    min="0"
                    step="1"
                    value={line.donGia}
                    onChange={(event) => handleLineChange(index, 'donGia', event.target.value)}
                  />
                </label>

                <div className="line-total">
                  <span>Thanh tien</span>
                  <strong>{formatCurrency(Number(line.soLuong || 0) * Number(line.donGia || 0))}</strong>
                </div>

                <button
                  type="button"
                  className="danger-btn small"
                  onClick={() => removeLine(index)}
                  disabled={form.chiTiets.length === 1}
                >
                  Xoa
                </button>
              </div>
            ))}
          </div>

          <div className="total-box">
            <span>Tong tam tinh</span>
            <strong>{formatCurrency(totalDraft)}</strong>
          </div>

          {error ? <div className="message error">{error}</div> : null}
          {success ? <div className="message success">{success}</div> : null}

          <div className="actions">
            <button className="primary-btn" type="submit" disabled={saving}>
              {saving ? 'Dang luu...' : editingId ? 'Cap nhat' : 'Tao moi'}
            </button>
            <button className="secondary-btn" type="button" onClick={resetForm}>
              Lam moi form
            </button>
          </div>
        </form>

        <section className="panel table-panel">
          <div className="panel-head">
            <div>
              <h2>Danh sach bao gia</h2>
              <p>
                Hien thi {filteredItems.length}/{items.length} ban ghi.
              </p>
            </div>
            {loading ? <span className="loading">Dang tai...</span> : null}
          </div>

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Ma bao gia</th>
                  <th>Khach hang</th>
                  <th>Nhan vien</th>
                  <th>San pham</th>
                  <th>Tong tien</th>
                  <th>Trang thai</th>
                  <th>Cap nhat</th>
                  <th>Hanh dong</th>
                </tr>
              </thead>
              <tbody>
                {filteredItems.length === 0 ? (
                  <tr>
                    <td colSpan="9" className="empty-row">
                      {loading ? 'Dang tai du lieu...' : 'Khong co du lieu phu hop'}
                    </td>
                  </tr>
                ) : (
                  filteredItems.map((item) => (
                    <tr key={item.id}>
                      <td>{item.id}</td>
                      <td>{item.maBaoGia}</td>
                      <td>{item.tenKhachHang ?? khachHangMap.get(String(item.khachHangId)) ?? item.khachHangId ?? '-'}</td>
                      <td>{item.tenNhanVien ?? nhanVienMap.get(String(item.nhanVienId)) ?? item.nhanVienId ?? '-'}</td>
                      <td>
                        <div className="stacked-cell">
                          {(item.chiTiets || []).map((line) => (
                            <span key={`${item.id}-${line.id ?? line.sanPhamId}`}>
                              {line.maSanPham} {line.tenSanPham} x{line.soLuong}
                            </span>
                          ))}
                        </div>
                      </td>
                      <td>{formatCurrency(item.tongTien)}</td>
                      <td>
                        <span className={`badge badge-${String(item.trangThai || '').toLowerCase()}`}>
                          {item.trangThai || '-'}
                        </span>
                      </td>
                      <td>{formatDateTime(item.updatedAt)}</td>
                      <td>
                        <div className="row-actions">
                          <button type="button" className="ghost-btn" onClick={() => handleEdit(item)}>
                            Sua
                          </button>
                          <button type="button" className="danger-btn" onClick={() => handleDelete(item.id)}>
                            Xoa
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </section>
      </section>
    </main>
  )
}

export default BaoGia
