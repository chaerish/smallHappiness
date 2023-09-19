package smu.likelion.smallHappiness.menu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smu.likelion.smallHappiness.menu.entity.Menu;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu,Long> {
    Page<Menu> findAllByStoreId(Long storeId, Pageable pageable);
    List<Menu> findAllByStoreId(Long storeId);
}