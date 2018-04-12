package org.apereo.cas.consent;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apereo.cas.authentication.Authentication;
import org.apereo.cas.authentication.principal.Service;
import org.apereo.cas.services.RegisteredService;
import org.apereo.cas.util.CollectionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This is {@link RestConsentRepository}.
 *
 * @author Misagh Moayyed
 * @since 5.1.0
 */
@Slf4j
@ToString
@AllArgsConstructor
public class RestConsentRepository implements ConsentRepository {

    private static final long serialVersionUID = 6583408864586270206L;

    private final transient RestTemplate restTemplate;

    private final String endpoint;

    @Override
    public Collection<ConsentDecision> findConsentDecisions(final String principal) {
        try {
            final var headers = new HttpHeaders();
            headers.setAccept(CollectionUtils.wrap(MediaType.APPLICATION_JSON));
            headers.put("principal", CollectionUtils.wrap(principal));
            final HttpEntity<String> entity = new HttpEntity<>(headers);
            final var result = restTemplate.exchange(this.endpoint, HttpMethod.GET, entity, List.class);
            if (result.getStatusCodeValue() == HttpStatus.OK.value()) {
                return result.getBody();
            }
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new ArrayList<>(0);
    }

    @Override
    public Collection<ConsentDecision> findConsentDecisions() {
        try {
            final var headers = new HttpHeaders();
            headers.setAccept(CollectionUtils.wrap(MediaType.APPLICATION_JSON));
            final HttpEntity<String> entity = new HttpEntity<>(headers);
            final var result = restTemplate.exchange(this.endpoint, HttpMethod.GET, entity, List.class);
            if (result.getStatusCodeValue() == HttpStatus.OK.value()) {
                return result.getBody();
            }
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new ArrayList<>(0);
    }

    @Override
    public ConsentDecision findConsentDecision(final Service service, final RegisteredService registeredService, final Authentication authentication) {
        try {
            final var headers = new HttpHeaders();
            headers.setAccept(CollectionUtils.wrap(MediaType.APPLICATION_JSON));
            headers.put("service", CollectionUtils.wrap(service.getId()));
            headers.put("principal", CollectionUtils.wrap(authentication.getPrincipal().getId()));
            final HttpEntity<String> entity = new HttpEntity<>(headers);
            final var result = restTemplate.exchange(this.endpoint, HttpMethod.GET, entity, ConsentDecision.class);
            if (result.getStatusCodeValue() == HttpStatus.OK.value()) {
                return result.getBody();
            }
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean storeConsentDecision(final ConsentDecision decision) {
        try {
            final var headers = new HttpHeaders();
            headers.setAccept(CollectionUtils.wrap(MediaType.APPLICATION_JSON));
            final HttpEntity<ConsentDecision> entity = new HttpEntity<>(decision, headers);
            final var result = restTemplate.exchange(this.endpoint, HttpMethod.POST, entity, ConsentDecision.class);
            return result.getStatusCodeValue() == HttpStatus.OK.value();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean deleteConsentDecision(final long decisionId) {
        try {
            final var headers = new HttpHeaders();
            headers.setAccept(CollectionUtils.wrap(MediaType.APPLICATION_JSON));
            final HttpEntity<Map> entity = new HttpEntity<>(headers);
            final var deleteEndpoint = this.endpoint.concat("/" + Long.toString(decisionId));
            final var result = restTemplate.exchange(deleteEndpoint,
                HttpMethod.DELETE, entity, Boolean.class);
            return result.getStatusCodeValue() == HttpStatus.OK.value();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean deleteConsentDecisions(final String principal) {
        try {
            final var headers = new HttpHeaders();
            headers.add("principal", principal);
            headers.setAccept(CollectionUtils.wrap(MediaType.APPLICATION_JSON));
            final HttpEntity<Map> entity = new HttpEntity<>(headers);
            final var result = restTemplate.exchange(this.endpoint, HttpMethod.DELETE, entity, Boolean.class);
            return result.getStatusCodeValue() == HttpStatus.OK.value();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }
}
