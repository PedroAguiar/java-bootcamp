package com.globant.controller;

import com.globant.aspect.annotation.Log;
import com.globant.aspect.annotation.Timer;
import com.globant.dto.ItemDTO;
import com.globant.model.Item;
import com.globant.service.ItemService;
import com.globant.util.DTOUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "payment-service", tags = {"ItemController"})
@Slf4j
@RequestMapping(value = "/item")
@RestController
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Log
    @Timer
    @ApiOperation(value = "Create an item", response = ItemDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created item"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @PutMapping(path = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDTO> createItem(@PathVariable(name = "name") String name) throws Exception {

        Validate.notBlank(name);
        final Item item = itemService.createItem(DTOUtils.toItemDTO("", name));
        log.info("Created item {}", item.getId());

        return new ResponseEntity<>(DTOUtils.toItemDTO(item), HttpStatus.CREATED);
    }

    @Log
    @Timer
    @ApiOperation(value = "Retrieve an item", response = ItemDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved item"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @GetMapping(path = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDTO> getItem(@PathVariable(name = "itemId") String itemId) throws Exception {

        final Item item = itemService.getItem(itemId);
        log.info("Returning itemId {} ", itemId);

        return new ResponseEntity<>(DTOUtils.toItemDTO(item), HttpStatus.OK);
    }

    @Log
    @Timer
    @ApiOperation(value = "Update an item", response = HttpStatus.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Successfully updated item"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @PostMapping(path = "/{itemId}/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus updateItem(@PathVariable(name = "itemId") String itemId,
                                 @PathVariable(name = "name") String name) throws Exception {

        validate(itemId);
        itemService.updateItem(DTOUtils.toItemDTO(itemId, name));

        return HttpStatus.ACCEPTED;
    }

    @Log
    @Timer
    @ApiOperation(value = "Delete an item", response = HttpStatus.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Successfully updated item"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @DeleteMapping(path = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus deleteItem(@PathVariable(name = "itemId") String itemId) throws Exception {

        validate(itemId);
        itemService.deleteItem(itemId);
        log.info("Deleted item {}", itemId);

        return HttpStatus.ACCEPTED;
    }

    private void validate(String itemId) throws Exception {
        Validate.notNull(itemService.getItem(itemId));
    }
}
