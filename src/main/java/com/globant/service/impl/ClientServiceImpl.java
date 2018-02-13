package com.globant.service.impl;

import com.globant.dto.ClientDTO;
import com.globant.model.Client;
import com.globant.model.Payment;
import com.globant.repository.ClientRepository;
import com.globant.repository.PaymentRepository;
import com.globant.service.ClientService;
import com.globant.util.EncryptingUtil;
import com.globant.util.ModelUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Client createClient(ClientDTO clientDTO) throws Exception {
        Validate.notNull(clientDTO);
        Client client = clientRepository.findByNameAndLastName(clientDTO.getName(), clientDTO.getLastName());
        Validate.isTrue(client != null);
        final List<Payment> payments = clientDTO.getPaymentIds().stream()
                .map(paymentId -> paymentRepository.getOne(EncryptingUtil.decryptId(paymentId)))
                .collect(Collectors.toList());
        client.setPayments(payments);
        return clientRepository.save(client);
    }


    @Override
    public Client getClient(String clientId) throws Exception {
        return clientRepository.getOne(EncryptingUtil.decryptId(clientId));
    }

    @Override
    public Client updateClient(ClientDTO clientDTO) throws Exception {
        Long clientId = clientRepository.getOne(EncryptingUtil.decryptId(clientDTO.getClientId())).getId();
        Client client = ModelUtils.toClient.apply(clientDTO);
        client.setId(clientId);
        List<Payment> payments = clientDTO.getPaymentIds().stream()
                .map(paymentId -> paymentRepository.getOne(EncryptingUtil.decryptId(paymentId)))
                .collect(Collectors.toList());
        client.setPayments(payments);
        return clientRepository.save(client);
    }

    @Override
    public void deleteClient(String clientId) throws Exception {
        clientRepository.delete(EncryptingUtil.decryptId(clientId));
    }

}
