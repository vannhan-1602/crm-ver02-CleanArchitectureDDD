package com.crm.infrastructure.sanpham;

import com.crm.domain.entities.LoaiSanPham;
import com.crm.domain.repositories.LoaiSanPhamRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public class LoaiSanPhamJpaRepoIml implements LoaiSanPhamRepo {
    private LoaiSanPhamJPArepo jparepo;
    public  LoaiSanPhamJpaRepoIml(LoaiSanPhamJPArepo jparepo){
        this.jparepo=jparepo;
    }
    @Override
    public List<LoaiSanPham>  findAll() {
        return jparepo.findAll();
    }
    @Override
    public Optional<LoaiSanPham> findById(Integer  id)
    {
        return jparepo.findById(id);
    }
    @Override
    public LoaiSanPham save(LoaiSanPham lsp)
    {
        return jparepo.save(lsp);
    }
    @Override
    public void Delete(LoaiSanPham lsp) {
        jparepo.delete(lsp);
    }


}
