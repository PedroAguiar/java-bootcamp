package com.globant.controller;

import com.globant.dto.ItemDTO;
import com.globant.model.Item;
import com.globant.service.ItemService;
import com.globant.util.DTOUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api
@Slf4j
@RequestMapping(value = "/item")
@RestController
public class ItemController {


    @Autowired
    private ItemService itemService;

    @PutMapping(path = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDTO> createItem(@PathVariable(name = "name") String name) throws Exception {
        if (log.isDebugEnabled())
            log.debug("Received CREATE item request for item with name {}" , name);

        Validate.notBlank(name);
        final Item item = itemService.createItem(DTOUtils.toItemDTO("", name));

        log.info("Created item {}", item.getId());

        if (log.isDebugEnabled())
            log.debug("Returning item {}", item.toString());

        return new ResponseEntity<>(DTOUtils.toItemDTO(item), HttpStatus.CREATED);
    }


    @GetMapping(path = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDTO> getItem(@PathVariable(name = "itemId") String itemId) throws Exception {
        if (log.isDebugEnabled())
            log.debug("Received GET item request for item {}", itemId);

        final Item item = itemService.getItem(itemId);

        log.info("Returning itemId {} ", itemId);

        return new ResponseEntity<>(DTOUtils.toItemDTO(item), HttpStatus.OK);
    }


    @PostMapping(path = "/{itemId}/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus updateItem(@PathVariable(name = "itemId") String itemId,
                                 @PathVariable(name = "name") String name) throws Exception {
        if (log.isDebugEnabled())
            log.debug("Received UPDATE item request for item {}", itemId);

        validate(itemId);

        Item item = itemService.updateItem(DTOUtils.toItemDTO(itemId, name));

        log.info("Updated item {}", itemId);

        if (log.isDebugEnabled())
            log.debug("Updated item {} data to: ", item.toString());

        return HttpStatus.ACCEPTED;
    }


    @DeleteMapping(path = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus deleteItem(@PathVariable(name = "itemId") String itemId) throws Exception {
        if (log.isDebugEnabled())
            log.debug("Received DELETE item request for item {}", itemId);

        validate(itemId);
        itemService.deleteItem(itemId);

        log.info("Deleted item {}", itemId);

        return HttpStatus.ACCEPTED;
    }

    private void validate(String itemId) throws Exception {
        Validate.notNull(itemService.getItem(itemId));
    }
}
