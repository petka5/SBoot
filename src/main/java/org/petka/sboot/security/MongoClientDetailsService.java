package org.petka.sboot.security;

import org.petka.sboot.persistence.documents.MongoClientDetails;
import org.petka.sboot.persistence.repositories.MongoClientDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.provider.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MongoClientDetailsService implements ClientDetailsService, ClientRegistrationService {


    @Autowired
    private MongoClientDetailsRepository clientDetailsRepository;


    private void init(){
        /*
        static final String GRANT_TYPE_PASSWORD = "password";
        static final String AUTHORIZATION_CODE = "authorization_code";
        static final String REFRESH_TOKEN = "refresh_token";
        static final String IMPLICIT = "implicit";
        static final String SCOPE_READ = "read";
        static final String SCOPE_WRITE = "write";
        static final String TRUST = "trust";*/


        MongoClientDetails mongoClientDetails = new MongoClientDetails();
        mongoClientDetails.setClientId("petka-client");
        mongoClientDetails.setClientSecret("$2a$04$rbSMkcj01sRE8cnQtMvmReYuc9rML6I9qeCV4Wv0NNfOlnVNpMVbK");
        Set<String> p = new HashSet<String>();
        p.add("password");
        mongoClientDetails.setAuthorizedGrantTypes(p);
        mongoClientDetails.setRefreshTokenValiditySeconds(6 * 60 * 60);
        mongoClientDetails.setAccessTokenValiditySeconds(1 * 60 * 60);
        Set<String> g = new HashSet<>();
        g.add("password"); g.add("authorization_code"); g.add("refresh_token");g.add("implicit");
        mongoClientDetails.setAuthorizedGrantTypes(g);
        List<GrantedAuthority> auth = new LinkedList<>();
        GrantedAuthority ga = new SimpleGrantedAuthority("admin");
        auth.add(ga);
        mongoClientDetails.setAuthorities(auth);
        Set<String> scope = new HashSet<>();
        scope.add("read");scope.add("write");
        mongoClientDetails.setScope(scope);
        clientDetailsRepository.save(mongoClientDetails);
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        //init();
        MongoClientDetails clientDetails = clientDetailsRepository.findByClientId(clientId);
        if (clientDetails == null) {
            throw new ClientRegistrationException(String.format("Client with id %s not found", clientId));
        }
        return clientDetails;
    }

    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        if (loadClientByClientId(clientDetails.getClientId()) == null) {
            MongoClientDetails mongoClientDetails =
                    new MongoClientDetails(clientDetails.getClientId(), clientDetails.getResourceIds(),
                            clientDetails.isSecretRequired(), NoOpPasswordEncoder.getInstance().encode(clientDetails.getClientSecret()),
                            clientDetails.isScoped(),
                            clientDetails.getScope(), clientDetails.getAuthorizedGrantTypes(), clientDetails.getRegisteredRedirectUri(),
                            clientDetails.getAuthorities(), clientDetails.getAccessTokenValiditySeconds(),
                            clientDetails.getRefreshTokenValiditySeconds(), clientDetails.isAutoApprove("true"),
                            clientDetails.getAdditionalInformation());
            clientDetailsRepository.save(mongoClientDetails);
        } else {
            throw new ClientAlreadyExistsException(String.format("Client with id %s already existed",
                    clientDetails.getClientId()));
        }
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        MongoClientDetails cDetails = clientDetailsRepository.findByClientId(clientDetails.getClientId());

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

        MongoClientDetails clientDetails = clientDetailsRepository.findByClientId(clientId);
        if (clientDetails == null) {
            throw new NoSuchClientException(String.format("Client with id %s not found", clientId));
        }
        clientDetails.setClientSecret(clientSecret);
    }

    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {

        MongoClientDetails clientDetails = clientDetailsRepository.findByClientId(clientId);
        if (clientDetails == null) {
            throw new NoSuchClientException(String.format("Client with id %s not found", clientId));
        }
        clientDetailsRepository.delete(clientDetails);
    }

    @Override
    public List<ClientDetails> listClientDetails() {
        List<ClientDetails> result = new ArrayList<ClientDetails>();
        Iterable<MongoClientDetails> all = clientDetailsRepository.findAll();
        all.forEach(result::add);
        return result;
    }
}
