package org.petka.sboot.security;

import org.petka.sboot.persistence.documents.CustomClientDetails;
import org.petka.sboot.persistence.repositories.CustomClientDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.provider.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomClientDetailsService implements ClientDetailsService, ClientRegistrationService {


    @Autowired
    private CustomClientDetailsRepository clientDetailsRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        CustomClientDetails clientDetails = clientDetailsRepository.findByClientId(clientId);
        if (clientDetails == null) {
            throw new ClientRegistrationException(String.format("Client with id %s not found", clientId));
        }
        return clientDetails;
    }

    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        if (loadClientByClientId(clientDetails.getClientId()) == null) {
            CustomClientDetails customClientDetails =
                    new CustomClientDetails(clientDetails.getClientId(), clientDetails.getResourceIds(),
                            clientDetails.isSecretRequired(), NoOpPasswordEncoder.getInstance().encode(clientDetails.getClientSecret()),
                            clientDetails.isScoped(),
                            clientDetails.getScope(), clientDetails.getAuthorizedGrantTypes(), clientDetails.getRegisteredRedirectUri(),
                            clientDetails.getAuthorities(), clientDetails.getAccessTokenValiditySeconds(),
                            clientDetails.getRefreshTokenValiditySeconds(), clientDetails.isAutoApprove("true"),
                            clientDetails.getAdditionalInformation());
            clientDetailsRepository.save(customClientDetails);
        } else {
            throw new ClientAlreadyExistsException(String.format("Client with id %s already existed",
                    clientDetails.getClientId()));
        }
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        CustomClientDetails cDetails = clientDetailsRepository.findByClientId(clientDetails.getClientId());

        if (cDetails == null) {
            throw new NoSuchClientException(String.format("Client with id %s not found", clientDetails.getClientId()));
        }
        cDetails.setResourceIds(clientDetails.getResourceIds());
        cDetails.setScope(clientDetails.getScope());
        cDetails.setAuthorizedGrantTypes(clientDetails.getAuthorizedGrantTypes());
        cDetails.setRegisteredRedirectUri(clientDetails.getRegisteredRedirectUri());
        cDetails.setAuthorities(clientDetails.getAuthorities());
        cDetails.setAccessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds());
        cDetails.setRefreshTokenValiditySeconds(clientDetails.getRefreshTokenValiditySeconds());
        cDetails.setAdditionalInformation(clientDetails.getAdditionalInformation());

        clientDetailsRepository.save(cDetails);
    }

    @Override
    public void updateClientSecret(String clientId, String clientSecret) throws NoSuchClientException {

        CustomClientDetails clientDetails = clientDetailsRepository.findByClientId(clientId);
        if (clientDetails == null) {
            throw new NoSuchClientException(String.format("Client with id %s not found", clientId));
        }
        clientDetails.setClientSecret(clientSecret);
    }

    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {

        CustomClientDetails clientDetails = clientDetailsRepository.findByClientId(clientId);
        if (clientDetails == null) {
            throw new NoSuchClientException(String.format("Client with id %s not found", clientId));
        }
        clientDetailsRepository.delete(clientDetails);
    }

    @Override
    public List<ClientDetails> listClientDetails() {
        List<ClientDetails> result = new ArrayList<ClientDetails>();
        Iterable<CustomClientDetails> all = clientDetailsRepository.findAll();
        all.forEach(result::add);
        return result;
    }
}
