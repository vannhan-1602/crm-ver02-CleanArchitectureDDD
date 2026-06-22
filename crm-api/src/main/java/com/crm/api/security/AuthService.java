package com.crm.api.security;

import com.crm.persistence.jpa.HtRoleJpaEntity;
import com.crm.persistence.jpa.HtUserJpaEntity;
import com.crm.persistence.jpa.HtUserModulePermissionJpaEntity;
import com.crm.persistence.jpa.ThongTinNhanSuJpaEntity;
import com.crm.persistence.repositories.HtRoleJPARepo;
import com.crm.persistence.repositories.HtUserJPARepo;
import com.crm.persistence.repositories.HtUserModulePermissionJPARepo;
import com.crm.persistence.repositories.ThongTinNhanSuJPARepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AuthService {
    private final HtUserJPARepo userRepo;
    private final HtRoleJPARepo roleRepo;
    private final HtUserModulePermissionJPARepo permissionRepo;
    private final ThongTinNhanSuJPARepo nhanSuRepo;
    private final PasswordService passwordService;
    private final TokenService tokenService;
    private final ModuleRegistry moduleRegistry;

    public AuthService(HtUserJPARepo userRepo,
                       HtRoleJPARepo roleRepo,
                       HtUserModulePermissionJPARepo permissionRepo,
                       ThongTinNhanSuJPARepo nhanSuRepo,
                       PasswordService passwordService,
                       TokenService tokenService,
                       ModuleRegistry moduleRegistry) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.permissionRepo = permissionRepo;
        this.nhanSuRepo = nhanSuRepo;
        this.passwordService = passwordService;
        this.tokenService = tokenService;
        this.moduleRegistry = moduleRegistry;
    }

    public LoginResult login(String username, String password) {
        HtUserJpaEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Sai ten dang nhap hoac mat khau"));
        if (!"Active".equalsIgnoreCase(user.getTrangThai())) {
            throw new IllegalArgumentException("Tai khoan khong hoat dong");
        }
        if (!passwordService.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Sai ten dang nhap hoac mat khau");
        }
        AuthUser authUser = toAuthUser(user);
        return new LoginResult(tokenService.create(authUser), tokenService.getTtlSeconds(), authUser,
                getEffectivePermissions(authUser));
    }

    public AuthUser toAuthUser(HtUserJpaEntity user) {
        String roleName = user.getRoleId() == null
                ? null
                : roleRepo.findById(user.getRoleId()).map(HtRoleJpaEntity::getTenRole).orElse(null);
        return new AuthUser(user.getId(), user.getUsername(), user.getRoleId(), roleName);
    }

    public boolean hasPermission(AuthUser user, String moduleKey, String action) {
        if (user.isAdmin()) {
            return true;
        }
        return permissionRepo.findByUserIdAndModuleKey(user.getId(), moduleKey)
                .map(permission -> switch (action) {
                    case "VIEW", "READ", "WRITE" -> hasAllPermissions(permission);
                    default -> false;
                })
                .orElse(false);
    }

    public List<ModulePermission> getPermissions(Integer userId) {
        return permissionRepo.findByUserId(userId).stream()
                .map(p -> new ModulePermission(
                        p.getModuleKey(),
                        Boolean.TRUE.equals(p.getCanView()),
                        Boolean.TRUE.equals(p.getCanRead()),
                        Boolean.TRUE.equals(p.getCanWrite())
                ))
                .toList();
    }

    public List<ModulePermission> getEffectivePermissions(AuthUser user) {
        if (user.isAdmin()) {
            return moduleRegistry.getModules().stream()
                    .map(module -> new ModulePermission(module.moduleKey(), true, true, true))
                    .toList();
        }
        return getPermissions(user.getId());
    }

    public List<HtUserJPARepo.UserSummaryProjection> getUsers() {
        return userRepo.findAllUserSummaries();
    }

    @Transactional
    public HtUserJpaEntity createUser(CreateUserInput input) {
        if (input.getUsername() == null || input.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username khong duoc de trong");
        }
        if (input.getPassword() == null || input.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password khong duoc de trong");
        }
        if (input.getNhanSuId() == null && (input.getHoTen() == null || input.getHoTen().isBlank())) {
            throw new IllegalArgumentException("Ho ten khong duoc de trong khi tao nhan su moi");
        }

        userRepo.findByUsername(input.getUsername()).ifPresent(existing -> {
            throw new IllegalArgumentException("Username da ton tai");
        });

        Integer nhanSuId = input.getNhanSuId();
        if (nhanSuId == null) {
            ThongTinNhanSuJpaEntity nhanSu = new ThongTinNhanSuJpaEntity();
            nhanSu.setHoTen(input.getHoTen());
            nhanSu.setEmail(input.getEmail());
            nhanSu.setSoDienThoai(input.getSoDienThoai());
            nhanSu.setPhongBanId(input.getPhongBanId());
            nhanSu.setChucVuId(input.getChucVuId());
            nhanSu.setTrangThai(1);
            nhanSuId = nhanSuRepo.save(nhanSu).getId();
        } else if (!nhanSuRepo.existsById(nhanSuId)) {
            throw new NoSuchElementException("Nhan su khong ton tai: " + nhanSuId);
        }

        HtUserJpaEntity user = new HtUserJpaEntity();
        user.setNhanSuId(nhanSuId);
        user.setUsername(input.getUsername());
        user.setPassword(passwordService.hash(input.getPassword()));
        user.setRoleId(input.getRoleId());
        user.setTrangThai(input.getTrangThai() == null ? "Active" : input.getTrangThai());
        return userRepo.save(user);
    }

    @Transactional
    public List<ModulePermission> replacePermissions(Integer userId, List<ModulePermission> permissions) {
        if (!userRepo.existsById(userId)) {
            throw new NoSuchElementException("User khong ton tai: " + userId);
        }
        permissions.forEach(this::validateCompletePermissionSet);
        permissionRepo.deleteByUserId(userId);
        LocalDateTime now = LocalDateTime.now();
        List<HtUserModulePermissionJpaEntity> entities = permissions.stream()
                .map(permission -> {
                    HtUserModulePermissionJpaEntity entity = new HtUserModulePermissionJpaEntity();
                    entity.setUserId(userId);
                    entity.setModuleKey(permission.getModuleKey());
                    entity.setCanView(permission.isCanView());
                    entity.setCanRead(permission.isCanRead());
                    entity.setCanWrite(permission.isCanWrite());
                    entity.setCreatedAt(now);
                    entity.setUpdatedAt(now);
                    return entity;
                })
                .toList();
        permissionRepo.saveAll(entities);
        return getPermissions(userId);
    }

    private boolean hasAllPermissions(HtUserModulePermissionJpaEntity permission) {
        return Boolean.TRUE.equals(permission.getCanView())
                && Boolean.TRUE.equals(permission.getCanRead())
                && Boolean.TRUE.equals(permission.getCanWrite());
    }

    private void validateCompletePermissionSet(ModulePermission permission) {
        long grantedCount = List.of(permission.isCanView(), permission.isCanRead(), permission.isCanWrite()).stream()
                .filter(Boolean::booleanValue)
                .count();
        if (grantedCount > 0 && grantedCount < 3) {
            throw new IllegalArgumentException(
                    "Module " + permission.getModuleKey() + " phai co du 3 quyen Xem, Doc, Ghi");
        }
    }

    public record LoginResult(String token, long expiresInSeconds, AuthUser user,
                              List<ModulePermission> permissions) {
    }

    public static class CreateUserInput {
        private Integer nhanSuId;
        private String hoTen;
        private String email;
        private String soDienThoai;
        private Integer phongBanId;
        private Integer chucVuId;
        private String username;
        private String password;
        private Integer roleId;
        private String trangThai;

        public Integer getNhanSuId() { return nhanSuId; }
        public String getHoTen() { return hoTen; }
        public String getEmail() { return email; }
        public String getSoDienThoai() { return soDienThoai; }
        public Integer getPhongBanId() { return phongBanId; }
        public Integer getChucVuId() { return chucVuId; }
        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public Integer getRoleId() { return roleId; }
        public String getTrangThai() { return trangThai; }

        public void setNhanSuId(Integer nhanSuId) { this.nhanSuId = nhanSuId; }
        public void setHoTen(String hoTen) { this.hoTen = hoTen; }
        public void setEmail(String email) { this.email = email; }
        public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
        public void setPhongBanId(Integer phongBanId) { this.phongBanId = phongBanId; }
        public void setChucVuId(Integer chucVuId) { this.chucVuId = chucVuId; }
        public void setUsername(String username) { this.username = username; }
        public void setPassword(String password) { this.password = password; }
        public void setRoleId(Integer roleId) { this.roleId = roleId; }
        public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    }
}
