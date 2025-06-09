package com.Stefan.BibliotecaUnical.mapper;

import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookDTO;
import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookSummaryDTO;
import com.Stefan.BibliotecaUnical.models.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    Book toEntity(BookDTO bookDTO);

    @Mapping(source = "shelf.id", target = "shelfID")
    BookDTO toDTO(Book book);

    BookSummaryDTO toSummaryDTO(Book book);
    List<BookSummaryDTO> toSummaryDTOList(List<Book> bookList);

    List<Book> toEntityList(List<BookDTO> bookDTOList);

    List<BookDTO> toDtoList(List<Book> bookList);
}
