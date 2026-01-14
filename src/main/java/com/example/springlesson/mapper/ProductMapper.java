package com.example.springlesson.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.springlesson.dto.ProductDTO;
import com.example.springlesson.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  /** 単体変換 */
  // Entity -> DTO
  ProductDTO toDto(Product entity);

  // DTO -> Entity
  Product toDto(ProductDTO dto);

  /** List 変換 */
  //Entity -> DTO
  List<ProductDTO> toDtoList(List<Product> entityList);

  //DTO -> Entity
  List<Product> toEntityList(List<ProductDTO> dtoList);
}
