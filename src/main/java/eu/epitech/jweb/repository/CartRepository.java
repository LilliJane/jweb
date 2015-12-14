package eu.epitech.jweb.repository;

import eu.epitech.jweb.domain.Cart;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Cart entity.
 */
public interface CartRepository extends JpaRepository<Cart,Long> {

    @Query("select cart from Cart cart where cart.user.login = ?#{principal.username}")
    List<Cart> findByUserIsCurrentUser();

    @Query("select distinct cart from Cart cart left join fetch cart.products")
    List<Cart> findAllWithEagerRelationships();

    @Query("select cart from Cart cart left join fetch cart.products where cart.id =:id")
    Cart findOneWithEagerRelationships(@Param("id") Long id);

}
