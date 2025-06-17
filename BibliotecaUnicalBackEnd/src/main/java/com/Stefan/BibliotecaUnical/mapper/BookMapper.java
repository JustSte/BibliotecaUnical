package com.Stefan.BibliotecaUnical.mapper;

import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookDTO;
import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookSummaryDTO;
import com.Stefan.BibliotecaUnical.DTO.FlatDTOs.BookFlatDTO;
import com.Stefan.BibliotecaUnical.models.Book;
import com.Stefan.BibliotecaUnical.models.Shelf;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "shelf", expression = "java(mapShelfFromId(bookDTO.getShelfID()))")
    Book toEntity(BookDTO bookDTO);
    @Mapping(source = "shelf.id", target = "shelfID")
    @Mapping(source = "shelf.location", target = "shelfLocation")
    BookDTO toDTO(Book book);

    BookSummaryDTO toSummaryDTO(Book book);
    BookSummaryDTO toSummaryDTOFromDTO(BookDTO bookDTO);
    BookDTO toDTOFromSummary(BookSummaryDTO bookSummaryDTO);
    List<BookSummaryDTO> toSummaryDTOList(List<Book> bookList);

    List<Book> toEntityList(List<BookDTO> bookDTOList);
    List<BookDTO> toDTOList(List<Book> bookList);


    Book toEntityFromFlat(BookFlatDTO bookFlatDTO);
    @Mapping(source = "shelf.id", target = "shelfId")
    BookFlatDTO toFlatFromEntity(Book book);

    default Shelf mapShelfFromId(Long shelfId)
    {
        if(shelfId == null) return null;
        Shelf shelf = new Shelf();
        shelf.setId(shelfId);
        return shelf;
    }

}
