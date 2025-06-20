package com.Stefan.BibliotecaUnical.controller;


import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairSummaryDTO;
import com.Stefan.BibliotecaUnical.DTO.LibraryTableDTOs.LibraryTableDTO;
import com.Stefan.BibliotecaUnical.helpers.TableChairHelper;
import com.Stefan.BibliotecaUnical.request.AddChairRequest;
import com.Stefan.BibliotecaUnical.request.ModifyTableRequest;
import com.Stefan.BibliotecaUnical.service.LibraryTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libraryTable")
@RequiredArgsConstructor
@Tag(name = "Table", description = "Manage tables of library")
public class LibraryTableController {
    private final LibraryTableService libraryTableService;
    private final TableChairHelper tableChairHelper;

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @GetMapping()
    @Operation(summary = "Retrive all tables")
    public ResponseEntity<Page<LibraryTableDTO>> getAllTables(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "8") int size)
    {

        return new ResponseEntity<>(libraryTableService.getAllTables(page,size), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Retrive a table by it's id")
    public ResponseEntity<LibraryTableDTO> getTableById(@PathVariable @NotNull Long id)
    {
        return new ResponseEntity<>(libraryTableService.getTableById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @GetMapping("/chairs/{id}")
    @Operation(summary = "Retrive a list of chairs from a table")
    public ResponseEntity<List<ChairSummaryDTO>> getChairsOfTable(@PathVariable @NotNull Long id)
    {
        return new ResponseEntity<>(libraryTableService.getChairsOfTable(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create a table")
    public ResponseEntity<LibraryTableDTO> saveLibraryTable(@RequestBody LibraryTableDTO libraryTableDTO)
    {
        return new ResponseEntity<>(libraryTableService.saveTable(libraryTableDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/addChairs")
    @Operation(summary = "Add 8 chairs to a table")
    public ResponseEntity<LibraryTableDTO> addChairToTable(@RequestBody AddChairRequest addChairRequest)
    {

        return new ResponseEntity<>((tableChairHelper.addChairsToTable(addChairRequest.getLibraryTableId())), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Modify a table")
    public ResponseEntity<LibraryTableDTO> modifyTable(@RequestBody ModifyTableRequest modifyTableRequest, @PathVariable @NotNull Long id)
    {
        return new ResponseEntity<>(libraryTableService.updateTable(modifyTableRequest, id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a table")
    public ResponseEntity<String> deleteTable(@PathVariable @NotNull Long id)
    {
        libraryTableService.deleteTable(id);
        return new ResponseEntity<>(("Table with id: " + id + " successfully deleted."), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/chairs/{id}")
    @Operation(summary = "Delete chairs from a table")
    public ResponseEntity<String> deleteChairsOfTable(@PathVariable @NotNull Long id)
    {
        tableChairHelper.deleteChairsOfTable(id);
        return new ResponseEntity<>(("Chairs of table: " + id + " deleted successfully"), HttpStatus.OK);
    }
}
