import React from 'react'
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom'

import HopDong from './page/HopDong'
import LeadManager from './components/LeadManager'
import BaoGia from './page/BaoGia'

function App() {
  return (
    <Router>
      <div style={layoutStyle}>
        <aside style={sidebarStyle}>
          <h2 style={brandStyle}>CRM SYSTEM</h2>
          <nav style={navStyle}>
            <Link to="/khach-hang" style={linkStyle}>
              Quan ly Khach hang
            </Link>
            <Link to="/nhan-vien" style={linkStyle}>
              Quan ly Nhan vien
            </Link>
            <Link to="/leads" style={linkStyle}>
              Quan ly Lead
            </Link>
            <Link to="/bao-gia" style={linkStyle}>
              Quan ly Bao gia
            </Link>
            <Link to="/hop-dong" style={linkStyle}>
              Quan ly Hop dong
            </Link>
          </nav>
        </aside>

        <main style={mainStyle}>
          <header style={headerStyle}>
            <div style={headerActionsStyle}>
              <span style={iconStyle}>🔔</span>
              <span style={iconStyle}>⚙️</span>
              <div style={avatarWrapStyle}>
                <img src="https://via.placeholder.com/35" alt="avatar" style={avatarStyle} />
              </div>
            </div>
          </header>

          <div style={contentStyle}>
            <Routes>
              <Route path="/leads" element={<LeadManager />} />
              <Route path="/bao-gia" element={<BaoGia />} />
              <Route path="/hop-dong" element={<HopDong />} />
              <Route path="/" element={<LeadManager />} />
            </Routes>
          </div>
        </main>
      </div>
    </Router>
  )
}

const layoutStyle = {
  display: 'flex',
  minHeight: '100vh',
  fontFamily: 'Arial, sans-serif',
}

const sidebarStyle = {
  width: '250px',
  backgroundColor: '#212b36',
  color: '#fff',
  padding: '20px',
  display: 'flex',
  flexDirection: 'column',
}

const brandStyle = {
  textAlign: 'center',
  marginBottom: '30px',
  fontSize: '20px',
}

const navStyle = {
  display: 'flex',
  flexDirection: 'column',
  gap: '15px',
}

const mainStyle = {
  flex: 1,
  backgroundColor: '#f9fafc',
  display: 'flex',
  flexDirection: 'column',
  height: '100vh',
}

const headerStyle = {
  height: '60px',
  backgroundColor: '#fff',
  borderBottom: '1px solid #ddd',
  display: 'flex',
  justifyContent: 'flex-end',
  alignItems: 'center',
  padding: '0 20px',
}

const headerActionsStyle = {
  display: 'flex',
  gap: '15px',
  alignItems: 'center',
}

const iconStyle = {
  cursor: 'pointer',
}

const avatarWrapStyle = {
  width: '35px',
  height: '35px',
  borderRadius: '50%',
  backgroundColor: '#ccc',
  overflow: 'hidden',
}

const avatarStyle = {
  width: '100%',
  height: '100%',
}

const contentStyle = {
  padding: '20px',
  flex: 1,
  overflowY: 'auto',
}

const linkStyle = {
  color: '#b1b9c3',
  textDecoration: 'none',
  padding: '10px',
  borderRadius: '4px',
  transition: 'background-color 0.2s',
  display: 'block',
}

export default App
