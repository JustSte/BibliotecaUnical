package com.Stefan.BibliotecaUnical.controller;


import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairSummaryDTO;
import com.Stefan.BibliotecaUnical.DTO.LibraryTableDTOs.LibraryTableDTO;
import com.Stefan.BibliotecaUnical.Helpers.TableChairHelper;
import com.Stefan.BibliotecaUnical.request.AddChairRequest;
import com.Stefan.BibliotecaUnical.request.ModifyTableRequest;
import com.Stefan.BibliotecaUnical.service.LibraryTableService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libraryTable")
@RequiredArgsConstructor
public class LibraryTableController {
    private final LibraryTableService libraryTableService;
    private final TableChairHelper tableChairHelper;

    @GetMapping()
    public ResponseEntity<Page<LibraryTableDTO>> getAllTables(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "8") int size)
    {

        return new ResponseEntity<>(libraryTableService.getAllTables(page,size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibraryTableDTO> getTableById(@PathVariable @NotNull Long id)
    {
        return new ResponseEntity<>(libraryTableService.getTableById(id), HttpStatus.OK);
    }

    @GetMapping("/chairs/{id}")
    public ResponseEntity<List<ChairSummaryDTO>> getChairsOfTable(@PathVariable @NotNull Long id)
    {
        return new ResponseEntity<>(libraryTableService.getChairsOfTable(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LibraryTableDTO> saveLibraryTable(@RequestBody LibraryTableDTO libraryTableDTO)
    {
        return new ResponseEntity<>(libraryTableService.saveTable(libraryTableDTO), HttpStatus.CREATED);
    }

    @PostMapping("/addChairs")
    public ResponseEntity<LibraryTableDTO> addChairToTable(@RequestBody AddChairRequest addChairRequest)
    {
        return new ResponseEntity<>((tableChairHelper.addChairsToTable(addChairRequest.getLibraryTableId(), addChairRequest.getChairList())), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibraryTableDTO> modifyTable(@RequestBody ModifyTableRequest modifyTableRequest, @PathVariable @NotNull Long id)
    {
        return new ResponseEntity<>(libraryTableService.updateTable(modifyTableRequest, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTable(@PathVariable @NotNull Long id)
    {
        libraryTableService.deleteTable(id);
        return new ResponseEntity<>(("Table with id: " + id + " successfully deleted."), HttpStatus.OK);
    }

    @DeleteMapping("/chairs/{id}")
    public ResponseEntity<String> deleteChairsOfTable(@PathVariable @NotNull Long id)
    {
        tableChairHelper.deleteChairsOfTable(id);
        return new ResponseEntity<>(("Chairs of table: " + id + " deleted successfully"), HttpStatus.OK);
    }
}
