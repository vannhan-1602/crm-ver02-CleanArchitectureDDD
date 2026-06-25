import { useEffect, useMemo, useState } from "react"
import { authFetch } from "../apiClient"
import "./BaoCaoThongKe.css"
import { ActionIcon } from "../moduleIcons.jsx";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8081"
const REPORT_TABS = [
  { id: "doanhSo", label: "Doanh số", helper: "Dòng tiền, doanh thu và xu hướng tháng" },
  { id: "kpi", label: "KPI", helper: "Chỉ số pipeline và tỷ lệ chuyển đổi" },
  { id: "hieuQua", label: "Hiệu quả hoạt động", helper: "Khối lượng xử lý và chất lượng vận hành" },
]

function formatMoney(value) {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
    maximumFractionDigits: 0,
  }).format(Number(value || 0))
}

function formatCount(value) {
  return new Intl.NumberFormat("vi-VN").format(Number(value || 0))
}

function formatPercent(value) {
  return `${Number(value || 0).toFixed(1)}%`
}

function formatMetricValue(metric) {
  const value = metric?.value ?? 0
  if (metric?.format === "currency") return formatMoney(value)
  if (metric?.format === "percent") return formatPercent(value)
  return formatCount(value)
}

function toneClass(tone = "neutral") {
  return `tone-${tone}`
}

function maxValue(items, field) {
  return items.reduce((max, item) => Math.max(max, Number(item?.[field] ?? 0)), 0)
}

function ratioWidth(value, max) {
  if (!max) return "0%"
  return `${Math.max(4, (Number(value || 0) / max) * 100)}%`
}

function BaoCaoThongKe() {
  const [report, setReport] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState("")
  const [activeTab, setActiveTab] = useState("doanhSo")

  const loadReport = async () => {
    setLoading(true)
    setError("")
    try {
      const response = await authFetch(`${API_BASE_URL}/api/bao-cao-thong-ke`, {
        cache: "no-store",
      })
      if (!response.ok) {
        throw new Error(`Không thể tải báo cáo thống kê (${response.status})`)
      }
      const data = await response.json()
      setReport(data)
    } catch (err) {
      setError(err.message || "Tải báo cáo thất bại")
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    void loadReport()
  }, [])

  const heroMetrics = useMemo(() => {
    if (!report) return []
    return [...report.finance.metrics.slice(0, 3), ...report.pipeline.metrics.slice(0, 3)]
  }, [report])

  const opportunityMax = useMemo(
    () => maxValue(report?.opportunityStages ?? [], "count"),
    [report],
  )
  const quoteMax = useMemo(() => maxValue(report?.quoteStatuses ?? [], "count"), [report])
  const monthMax = useMemo(
    () => maxValue(report?.monthlySummaries ?? [], "invoiceValue"),
    [report],
  )

  const activeTabLabel = REPORT_TABS.find((tab) => tab.id === activeTab)?.label ?? ""
  const financeMetrics = report?.finance.metrics ?? []
  const pipelineMetrics = report?.pipeline.metrics ?? []
  const operationMetrics = report?.operation.metrics ?? []
  const monthlySummaries = report?.monthlySummaries ?? []
  const opportunityStages = report?.opportunityStages ?? []
  const quoteStatuses = report?.quoteStatuses ?? []
  const activeMetrics =
    activeTab === "doanhSo"
      ? financeMetrics.slice(0, 6)
      : activeTab === "kpi"
        ? pipelineMetrics.slice(0, 6)
        : operationMetrics.slice(0, 6)
  const activeTabInfo = REPORT_TABS.find((tab) => tab.id === activeTab)

  return (
    <div className="report-page">
      <section className="report-hero">
        <div>
          <p className="eyebrow">Báo cáo thống kê</p>
          <h1>Dashboard tổng hợp CRM</h1>
          <p className="subtitle">
            Theo dõi doanh số, pipeline, báo giá và hiệu quả vận hành từ một màn hình.
          </p>
        </div>
        <div className="hero-actions">
          <button className="refresh-btn btn-icon" onClick={loadReport} disabled={loading}>
            <ActionIcon name="refresh" />
            {loading ? "Đang tải..." : "Làm mới báo cáo"}
          </button>
          <div className="generated-at">
            <span>Cập nhật gần nhất</span>
            <strong>
              {report?.generatedAt ? new Date(report.generatedAt).toLocaleString("vi-VN") : "-"}
            </strong>
          </div>
        </div>
      </section>

      {error ? <div className="alert error">{error}</div> : null}
      {loading && !report ? (
        <div className="alert loading">Đang tổng hợp dữ liệu báo cáo...</div>
      ) : null}

      {report ? (
        <>
          <section className="report-summary-row" aria-label="Chỉ số nổi bật">
            {heroMetrics.slice(0, 4).map((metric) => (
              <article key={`hero-${metric.label}-${metric.hint}`} className={`summary-card ${toneClass(metric.tone)}`}>
                <span>{metric.label}</span>
                <strong>{formatMetricValue(metric)}</strong>
                <p>{metric.hint}</p>
              </article>
            ))}
          </section>

          <nav className="report-tabs" aria-label="Chuyển tab báo cáo thống kê">
            {REPORT_TABS.map((tab) => (
              <button
                key={tab.id}
                type="button"
                className={`report-tab ${activeTab === tab.id ? "active" : ""}`}
                onClick={() => setActiveTab(tab.id)}
              >
                <span>{tab.label}</span>
              </button>
            ))}
          </nav>

          <div className="tab-context">
            <div>
              <span>Đang xem</span>
              <strong>{activeTabLabel}</strong>
            </div>
            <p>{activeTabInfo?.helper}</p>
          </div>

          <section className="metric-grid">
            {activeMetrics.map((metric) => (
              <article
                key={`${activeTab}-${metric.label}-${metric.hint}`}
                className={`metric-card ${toneClass(metric.tone)}`}
              >
                <div className="metric-card-head">
                  <span>{metric.label}</span>
                </div>
                <strong>{formatMetricValue(metric)}</strong>
                <p>{metric.hint}</p>
              </article>
            ))}
          </section>

          {activeTab === "doanhSo" ? (
            <>
              <section className="overview-grid">
                <article className="panel">
                  <div className="panel-head">
                    <div>
                      <p className="panel-kicker">{report.finance.title}</p>
                      <h2>Dòng tiền và doanh số</h2>
                    </div>
                  </div>
                  <div className="stacked-list">
                    {financeMetrics.map((metric) => (
                      <div key={metric.label} className={`stacked-item ${toneClass(metric.tone)}`}>
                        <div>
                          <span>{metric.label}</span>
                          <p>{metric.hint}</p>
                        </div>
                        <strong>{formatMetricValue(metric)}</strong>
                      </div>
                    ))}
                  </div>
                </article>

                <article className="panel">
                  <div className="panel-head">
                    <div>
                      <p className="panel-kicker">Xu hướng 6 tháng</p>
                      <h2>Nhịp tăng trưởng theo thời gian</h2>
                    </div>
                  </div>
                  <div className="month-list">
                    {monthlySummaries.map((month) => (
                      <div key={month.month} className="month-card">
                        <div className="month-head">
                          <strong>{month.month}</strong>
                          <span>{formatMoney(month.netCashFlow)}</span>
                        </div>
                        <div className="month-bars">
                          <div>
                            <label>Doanh số</label>
                            <div className="bar-track">
                              <div
                                className="bar-fill revenue"
                                style={{ width: ratioWidth(month.invoiceValue, monthMax) }}
                              />
                            </div>
                            <small>{formatMoney(month.invoiceValue)}</small>
                          </div>
                          <div>
                            <label>Đã thu</label>
                            <div className="bar-track">
                              <div
                                className="bar-fill cash"
                                style={{ width: ratioWidth(month.cashReceived, monthMax) }}
                              />
                            </div>
                            <small>{formatMoney(month.cashReceived)}</small>
                          </div>
                          <div>
                            <label>Chi phí</label>
                            <div className="bar-track">
                              <div
                                className="bar-fill cost"
                                style={{ width: ratioWidth(month.expenseValue, monthMax) }}
                              />
                            </div>
                            <small>{formatMoney(month.expenseValue)}</small>
                          </div>
                        </div>
                        <div className="month-meta">
                          <span>Lead {formatCount(month.leadCount)}</span>
                          <span>HĐ {formatCount(month.contractCount)}</span>
                          <span>HD {formatCount(month.activityCount)}</span>
                          <span>Báo giá {formatCount(month.quoteCount)}</span>
                          <span>Opportunity {formatMoney(month.opportunityValue)}</span>
                        </div>
                      </div>
                    ))}
                  </div>
                </article>
              </section>

              <section className="overview-grid">
                <article className="panel">
                  <div className="panel-head">
                    <div>
                      <p className="panel-kicker">Phễu bán hàng</p>
                      <h2>Phân bổ cơ hội theo giai đoạn</h2>
                    </div>
                  </div>
                  <div className="breakdown-list">
                    {opportunityStages.length ? (
                      opportunityStages.map((item) => (
                        <div key={item.label} className="breakdown-row">
                          <div className="breakdown-meta">
                            <strong>{item.label}</strong>
                            <span>
                              {formatCount(item.count)} cơ hội · {formatMoney(item.amount)} doanh
                              thu kỳ vọng
                            </span>
                          </div>
                          <div className="bar-track">
                            <div
                              className="bar-fill opportunity"
                              style={{ width: ratioWidth(item.count, opportunityMax) }}
                            />
                          </div>
                          <small>{formatPercent(item.rate)}</small>
                        </div>
                      ))
                    ) : (
                      <p className="empty-note">Chưa có dữ liệu cơ hội bán hàng.</p>
                    )}
                  </div>
                </article>

                <article className="panel">
                  <div className="panel-head">
                    <div>
                      <p className="panel-kicker">Báo giá</p>
                      <h2>Trạng thái báo giá</h2>
                    </div>
                  </div>
                  <div className="breakdown-list">
                    {quoteStatuses.length ? (
                      quoteStatuses.map((item) => (
                        <div key={item.label} className="breakdown-row">
                          <div className="breakdown-meta">
                            <strong>{item.label}</strong>
                            <span>
                              {formatCount(item.count)} báo giá · {formatMoney(item.amount)}
                            </span>
                          </div>
                          <div className="bar-track">
                            <div
                              className="bar-fill quote"
                              style={{ width: ratioWidth(item.count, quoteMax) }}
                            />
                          </div>
                          <small>{formatPercent(item.rate)}</small>
                        </div>
                      ))
                    ) : (
                      <p className="empty-note">Chưa có dữ liệu báo giá.</p>
                    )}
                  </div>
                </article>
              </section>
            </>
          ) : null}

          {activeTab === "kpi" ? (
            <section className="overview-grid">
              <article className="panel">
                <div className="panel-head">
                  <div>
                    <p className="panel-kicker">{report.pipeline.title}</p>
                    <h2>KPI quy trình</h2>
                  </div>
                </div>
                <div className="stacked-list compact">
                  {pipelineMetrics.map((metric) => (
                    <div key={metric.label} className={`stacked-item ${toneClass(metric.tone)}`}>
                      <div>
                        <span>{metric.label}</span>
                        <p>{metric.hint}</p>
                      </div>
                      <strong>{formatMetricValue(metric)}</strong>
                    </div>
                  ))}
                </div>
              </article>

              <article className="panel">
                <div className="panel-head">
                  <div>
                    <p className="panel-kicker">Tổng quan KPI</p>
                    <h2>Chỉ số đo lường chính</h2>
                  </div>
                </div>
                <div className="stacked-list compact">
                  {heroMetrics
                    .filter((metric) => metric)
                    .map((metric) => (
                      <div
                        key={`${activeTab}-${metric.label}-${metric.hint}`}
                        className={`stacked-item ${toneClass(metric.tone)}`}
                      >
                        <div>
                          <span>{metric.label}</span>
                          <p>{metric.hint}</p>
                        </div>
                        <strong>{formatMetricValue(metric)}</strong>
                      </div>
                    ))}
                </div>
              </article>
            </section>
          ) : null}

          {activeTab === "hieuQua" ? (
            <section className="overview-grid">
              <article className="panel">
                <div className="panel-head">
                  <div>
                    <p className="panel-kicker">{report.operation.title}</p>
                    <h2>Hiệu quả hoạt động</h2>
                  </div>
                </div>
                <div className="stacked-list">
                  {operationMetrics.map((metric) => (
                    <div key={metric.label} className={`stacked-item ${toneClass(metric.tone)}`}>
                      <div>
                        <span>{metric.label}</span>
                        <p>{metric.hint}</p>
                      </div>
                      <strong>{formatMetricValue(metric)}</strong>
                    </div>
                  ))}
                </div>
              </article>

              <article className="panel">
                <div className="panel-head">
                  <div>
                    <p className="panel-kicker">Tóm tắt</p>
                    <h2>{activeTabLabel}</h2>
                  </div>
                </div>
                <p className="empty-note">
                  Tab này tập trung vào các chỉ số vận hành: mức độ xử lý công việc, tốc độ phản
                  hồi và chất lượng thực thi theo toàn quy trình.
                </p>
              </article>
            </section>
          ) : null}
        </>
      ) : null}
    </div>
  )
}

export default BaoCaoThongKe
