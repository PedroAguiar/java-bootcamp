package com.globant.controller;

import com.globant.aspect.annotation.Log;
import com.globant.aspect.annotation.Timer;
import com.globant.dto.ClientDTO;
import com.globant.model.Client;
import com.globant.service.ClientService;
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

import java.util.ArrayList;
import java.util.List;

@Api(value = "payment-service", tags = {"ClientController"})
@Slf4j
@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Log
    @ApiOperation(value = "Create a client", response = ClientDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created client"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @PutMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> createClient(@RequestParam(name = "name") String name,
                                                  @RequestParam(name = "lastName") String lastName,
                                                  @RequestParam(name = "description") String description) throws Exception {

        Validate.notBlank(name, lastName, description);
        final Client client = clientService.saveClient(DTOUtils.toClientDTO("", name, lastName, description, new ArrayList<>()));
        log.info("Created client {}", client.getId());

        return new ResponseEntity<>(DTOUtils.toClientDTO(client), HttpStatus.CREATED);
    }

    @Timer
    @ApiOperation(value = "Retrieve a client", response = ClientDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved client"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @GetMapping(path = "/retrieve", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> getClient(@RequestParam(name = "clientId") String clientId) throws Exception {

        final Client client = clientService.getClient(clientId);
        log.info("Returning client {} ", clientId);

        return new ResponseEntity<>(DTOUtils.toClientDTO(client), HttpStatus.OK);
    }

    @Timer
    @ApiOperation(value = "Update a client", response = ClientDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Successfully updated client"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @PostMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus updateClient(@RequestParam(name = "clientId") String clientId,
                                   @RequestParam(name = "name") String name,
                                   @RequestParam(name = "lastName") String lastName,
                                   @RequestParam(name = "description") String description,
                                   @RequestParam(name = "paymentIds") List<String> paymentIds) throws Exception {

        validate(clientId);
        clientService.updateClient(DTOUtils.toClientDTO(clientId, name, lastName, description, paymentIds));
        log.info("Updated client {}", clientId);

        return HttpStatus.ACCEPTED;
    }

    @Timer
    @ApiOperation(value = "Delete a client", response = ClientDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Successfully deleted client"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @DeleteMapping(path = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus deleteClient(@RequestParam(name = "clientId") String clientId) throws Exception {

        Client client = clientService.getClient(clientId);
        Validate.isTrue(client != null, "Client " + clientId + " did not exist.");
        clientService.deleteClient(clientId);

        return HttpStatus.ACCEPTED;
    }

    private void validate(String clientId) throws Exception {
        Validate.notNull(clientService.getClient(clientId));
    }
}
