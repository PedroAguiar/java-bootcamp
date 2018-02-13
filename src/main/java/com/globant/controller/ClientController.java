package com.globant.controller;

import com.globant.dto.ClientDTO;
import com.globant.model.Client;
import com.globant.service.ClientService;
import com.globant.service.OrderService;
import com.globant.service.PaymentService;
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
@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ClientService clientService;


    @PutMapping(path = "/{clientId}/{orderId}/{amount}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> createClient(@PathVariable(name = "name") String name,
                                                  @PathVariable(name = "lastName") String lastName,
                                                  @PathVariable(name = "description") String description) throws Exception {
        if (log.isDebugEnabled())
            log.debug("Received CREATE client request for client " + name + " " + lastName);

        Validate.notBlank(name, lastName, description);
        final Client client = clientService.createClient(DTOUtils.toClientDTO("", name, lastName, description));

        log.info("Created client {}", client.getId());

        if (log.isDebugEnabled())
            log.debug("Returning client {}", client.toString());

        return new ResponseEntity<>(DTOUtils.toClientDTO(client), HttpStatus.CREATED);
    }


    @GetMapping(path = "/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> getClient(@PathVariable(name = "clientId") String clientId) throws Exception {
        if (log.isDebugEnabled())
            log.debug("Received GET client request for client {}", clientId);

        final Client client = clientService.getClient(clientId);

        log.info("Returning client {} ", clientId);

        return new ResponseEntity<>(DTOUtils.toClientDTO(client), HttpStatus.OK);
    }


    @PostMapping(path = "/{clientId}/{name}/{lastName}}/{description}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus updateClient(@PathVariable(name = "clientId") String clientId,
                                   @PathVariable(name = "name") String name,
                                   @PathVariable(name = "lastName") String lastName,
                                   @PathVariable(name = "description") String description) throws Exception {
        if (log.isDebugEnabled())
            log.debug("Received UPDATE client request for client {}", clientId);

        validate(clientId);

        Client client = clientService.updateClient(DTOUtils.toClientDTO(clientId, name, lastName, description));


        log.info("Updated client {}", clientId);

        if (log.isDebugEnabled())
            log.debug("Updated client {} data to: ", client.toString());

        return HttpStatus.ACCEPTED;
    }


    @DeleteMapping(path = "/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus deleteClient(@PathVariable(name = "clientId") String clientId) throws Exception {
        if (log.isDebugEnabled())
            log.debug("Received DELETE client request for client {}", clientId);

        Client client = clientService.getClient(clientId);
        Validate.isTrue(client != null, "Client " + clientId + " did not exist.");
        clientService.deleteClient(clientId);

        if (log.isDebugEnabled())
            log.debug("Deleted client {}", clientId);

        return HttpStatus.ACCEPTED;
    }

    private void validate(String clientId) throws Exception {
        Validate.notNull(clientService.getClient(clientId));
    }
}
