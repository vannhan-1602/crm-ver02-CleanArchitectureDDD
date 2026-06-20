import { useEffect, useMemo, useState } from 'react'
import { authFetch } from '../apiClient'
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
      const response = await authFetch(`${API_BASE_URL}/api/sanpham`)
      if (!response.ok) {
        throw new Error(`Không thể tải sản phẩm (${response.status})`)
      }
      const data = await response.json()
      setProducts(Array.isArray(data) ? data : [])
    } catch (err) {
      setError(err.message || 'Tải danh sách sản phẩm thất bại')
    }
  }

  const loadKhachHang = async () => {
    try {
      const response = await authFetch(`${API_BASE_URL}/api/khach-hang`)
      if (!response.ok) return
      const data = await response.json()
      setKhachHangList(Array.isArray(data) ? data : [])
    } catch {
      // Dropdown khách hàng chỉ là dữ liệu hỗ trợ.
    }
  }

  const loadNhanVien = async () => {
    try {
      const response = await authFetch(`${API_BASE_URL}/api/nhan-vien`)
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
      const response = await authFetch(`${API_BASE_URL}/api/bao-gia`, {
        cache: 'no-store',
      })
      if (!response.ok) {
        throw new Error(`Không thể tải danh sách báo giá (${response.status})`)
      }
      const data = await response.json()
      setItems(Array.isArray(data) ? data : [])
    } catch (err) {
      setError(err.message || 'Tải danh sách báo giá thất bại')
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
    if (!String(form.maBaoGia ?? '').trim()) return 'Mã báo giá không được rỗng'
    if (!String(form.khachHangId ?? '').trim()) return 'Khách hàng không được rỗng'
    const validLines = form.chiTiets.filter((line) => line.sanPhamId)
    if (validLines.length === 0) return 'Báo giá phải có ít nhất 1 sản phẩm'
    for (const line of validLines) {
      if (!line.sanPhamId) return 'Vui lòng chọn sản phẩm'
      if (!Number(line.soLuong) || Number(line.soLuong) <= 0) return 'Số lượng phải lớn hơn 0'
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
      const response = await authFetch(
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
            ? `Cập nhật thất bại (${response.status})`
            : `Tạo mới thất bại (${response.status})`,
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
      setSuccess(editingId ? 'Cập nhật báo giá thành công' : 'Tạo báo giá thành công')
    } catch (err) {
      setError(err.message || 'Không thể lưu báo giá')
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
    const confirmed = window.confirm('Bạn có muốn xóa báo giá này không?')
    if (!confirmed) return

    setError('')
    setSuccess('')

    try {
      const response = await authFetch(`${API_BASE_URL}/api/bao-gia/${id}`, {
        method: 'DELETE',
      })
      if (!response.ok && response.status !== 204) {
        throw new Error(`Xóa thất bại (${response.status})`)
      }
      await loadBaoGia()
      if (editingId === id) resetForm()
      setSuccess('Xóa báo giá thành công')
    } catch (err) {
      setError(err.message || 'Không thể xóa báo giá')
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
          <p className="eyebrow">CRM / Báo giá</p>
          <h1>Quản lý báo giá</h1>
          <p className="subtitle">
            Tạo báo giá có nhiều dòng sản phẩm, tự tính tổng tiền và lưu qua API backend.
          </p>
        </div>

        <div className="toolbar">
          <input
            className="search"
            type="search"
            placeholder="Tìm theo mã, khách hàng, sản phẩm, trạng thái..."
            value={search}
            onChange={(event) => setSearch(event.target.value)}
          />
          <button className="secondary-btn" type="button" onClick={loadBaoGia}>
            Tải lại
          </button>
        </div>
      </section>

      <section className="stats-row">
        <article className="stat-card">
          <span>Tổng số</span>
          <strong>{stats.total}</strong>
        </article>
        <article className="stat-card">
          <span>Nháp</span>
          <strong>{stats.drafts}</strong>
        </article>
        <article className="stat-card">
          <span>Đã gửi</span>
          <strong>{stats.sent}</strong>
        </article>
        <article className="stat-card">
          <span>Chấp nhận</span>
          <strong>{stats.accepted}</strong>
        </article>
        <article className="stat-card">
          <span>Từ chối</span>
          <strong>{stats.rejected}</strong>
        </article>
      </section>

      <section className="content-grid">
        <form className="panel form-panel" onSubmit={handleSubmit}>
          <div className="panel-head">
            <div>
              <h2>{editingId ? 'Cập nhật báo giá' : 'Tạo báo giá mới'}</h2>
              <p>Dữ liệu sẽ gọi vào API `api/bao-gia`.</p>
            </div>
            {editingId ? (
              <button className="ghost-btn" type="button" onClick={resetForm}>
                Hủy sửa
              </button>
            ) : null}
          </div>

          <label>
            Mã báo giá
            <input
              name="maBaoGia"
              value={form.maBaoGia}
              onChange={handleFieldChange}
              placeholder="BG-0001"
            />
          </label>

          <div className="two-col">
            <label>
              Khách hàng
              <select
                name="khachHangId"
                value={form.khachHangId}
                onChange={handleFieldChange}
              >
                <option value="">Chọn khách hàng</option>
                {khachHangList.map((item) => (
                  <option key={item.id} value={item.id}>
                    {item.tenKhachHang ?? `KH #${item.id}`}
                  </option>
                ))}
              </select>
            </label>

            <label>
              Nhân viên
              <select
                name="nhanVienId"
                value={form.nhanVienId}
                onChange={handleFieldChange}
              >
                <option value="">Chọn nhân viên</option>
                {nhanVienList.map((item) => (
                  <option key={item.id} value={item.id}>
                    {item.hoTen ?? item.tenNhanVien ?? `NV #${item.id}`}
                  </option>
                ))}
              </select>
            </label>
          </div>

          <label>
            Trạng thái
            <select name="trangThai" value={form.trangThai} onChange={handleFieldChange}>
              <option value="Nhap">Nháp</option>
              <option value="DaGui">Đã gửi</option>
              <option value="TuChoi">Từ chối</option>
              <option value="ChapNhan">Chấp nhận</option>
            </select>
          </label>

          <div className="detail-header">
            <div>
              <h3>Chi tiết sản phẩm</h3>
              <p>Chọn sản phẩm từ danh sách, số lượng và đơn giá sẽ tính ngay bên dưới.</p>
            </div>
            <button type="button" className="ghost-btn" onClick={addLine}>
              + Thêm dòng
            </button>
          </div>

          <div className="detail-list">
            {form.chiTiets.map((line, index) => (
              <div className="detail-row" key={`${index}-${line.sanPhamId}`}>
                <label>
                  Sản phẩm
                  <select
                    value={line.sanPhamId}
                    onChange={(event) => handleLineChange(index, 'sanPhamId', event.target.value)}
                  >
                    <option value="">Chọn sản phẩm</option>
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
                  Số lượng
                  <input
                    type="number"
                    min="1"
                    value={line.soLuong}
                    onChange={(event) => handleLineChange(index, 'soLuong', event.target.value)}
                  />
                </label>

                <label>
                  Đơn giá
                  <input
                    type="number"
                    min="0"
                    step="1"
                    value={line.donGia}
                    onChange={(event) => handleLineChange(index, 'donGia', event.target.value)}
                  />
                </label>

                <div className="line-total">
                  <span>Thành tiền</span>
                  <strong>{formatCurrency(Number(line.soLuong || 0) * Number(line.donGia || 0))}</strong>
                </div>

                <button
                  type="button"
                  className="danger-btn small"
                  onClick={() => removeLine(index)}
                  disabled={form.chiTiets.length === 1}
                >
                  Xóa
                </button>
              </div>
            ))}
          </div>

          <div className="total-box">
            <span>Tổng tạm tính</span>
            <strong>{formatCurrency(totalDraft)}</strong>
          </div>

          {error ? <div className="message error">{error}</div> : null}
          {success ? <div className="message success">{success}</div> : null}

          <div className="actions">
            <button className="primary-btn" type="submit" disabled={saving}>
              {saving ? 'Đang lưu...' : editingId ? 'Cập nhật' : 'Tạo mới'}
            </button>
            <button className="secondary-btn" type="button" onClick={resetForm}>
              Làm mới form
            </button>
          </div>
        </form>

        <section className="panel table-panel">
          <div className="panel-head">
            <div>
              <h2>Danh sách báo giá</h2>
              <p>
                Hiển thị {filteredItems.length}/{items.length} bản ghi.
              </p>
            </div>
            {loading ? <span className="loading">Đang tải...</span> : null}
          </div>

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Mã báo giá</th>
                  <th>Khách hàng</th>
                  <th>Nhân viên</th>
                  <th>Sản phẩm</th>
                  <th>Tổng tiền</th>
                  <th>Trạng thái</th>
                  <th>Cập nhật</th>
                  <th>Hành động</th>
                </tr>
              </thead>
              <tbody>
                {filteredItems.length === 0 ? (
                  <tr>
                    <td colSpan="9" className="empty-row">
                      {loading ? 'Đang tải dữ liệu...' : 'Không có dữ liệu phù hợp'}
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
                            Sửa
                          </button>
                          <button type="button" className="danger-btn" onClick={() => handleDelete(item.id)}>
                            Xóa
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
