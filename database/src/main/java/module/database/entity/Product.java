package module.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Product 엔티티
 */
@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    /**
     * 기본 키, 자동 증가
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 브랜드 (최대 100자, 필수)
     */
    @Column(length = 100, nullable = false)
    private String brand;

    /**
     * SKU (최대 100자, 필수)
     */
    @Column(length = 100, nullable = false)
    private String sku;

    /**
     * 상품명 (최대 100자)
     */
    @Column(length = 100)
    private String name;

    /**
     * 상세 링크 (최대 100자)
     */
    @Column(length = 100)
    private String link;

    /**
     * 이미지 경로 (최대 100자)
     */
    @Column(name = "image_src", length = 100)
    private String imageSrc;
}
