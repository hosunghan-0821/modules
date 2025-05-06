package module.database.repository;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import module.database.entity.Monitor;
import module.database.entity.Product;
import module.database.entity.ProductSize;
import module.database.entity.QProduct;
import module.database.entity.QProductSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static module.database.entity.QMonitor.monitor;
import static module.database.entity.QProduct.product;
import static module.database.entity.QProductSize.productSize;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    public Page<Product> getProducts(Pageable pageable, String keyword) {

        BooleanExpression predicate = keywordPredicate(keyword);

        List<Product> content = jpaQueryFactory
                .selectFrom(product)
                .where(predicate)
                .orderBy(product.id.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(product.count())
                .from(product)
                .where(predicate);


        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public Optional<Product> findById(Long id) {
        Product findProduct = jpaQueryFactory.selectFrom(product).where(product.id.eq(id)).fetchOne();
        return Optional.ofNullable(findProduct);
    }

    public void saveAllProductSize(List<ProductSize> productSizes) {
        productSizes.forEach(entityManager::persist);
        entityManager.flush();
    }

    public void deleteAllProductSize(List<Long> deleteIds) {
        jpaQueryFactory.delete(productSize).where(productSize.id.in(deleteIds)).execute();
        entityManager.flush();
    }

    public void saveProduct(Product product) {
        entityManager.persist(product);
        entityManager.flush();
    }

    public void deleteAllProduct(List<Long> productIds) {
        jpaQueryFactory.delete(productSize).where(productSize.product.id.in(productIds)).execute();
        jpaQueryFactory.delete(product).where(product.id.in(productIds)).execute();
    }

    public List<Product> findAllByIds(List<Long> productIds) {
        return jpaQueryFactory.selectFrom(product).where(product.id.in(productIds)).fetch();
    }

    /**
     * keyword 가 널이거나 빈 문자열이면 null 리턴(조건 미적용),
     * 그 외엔 SKU OR name OR description 에 LIKE 검색
     */
    private BooleanExpression keywordPredicate(String keyword) {
        if (!hasText(keyword)) {
            return null;
        }

        return product.sku.containsIgnoreCase(keyword)
                .or(product.boutique.equalsIgnoreCase(keyword))
                .or(product.brand.equalsIgnoreCase(keyword));
    }
}
