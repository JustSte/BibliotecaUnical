package com.Stefan.BibliotecaUnical.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class KeycloakUserService {

    private static final String MISSED_CONFIRMATIONS_ATTR = "missedConfirmations";
    private static final int MAX_MISSED_CONFIRMATIONS = 5;

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public void incrementMissedConfirmations(String userId)
    {
        UserResource userResource = keycloak.realm(realm).users().get(userId);
        UserRepresentation user = userResource.toRepresentation();

        Map<String, List<String>> attributes = user.getAttributes();
        if(attributes == null)
        {
            attributes = new HashMap<>();
        }

        int currentCount = 0;
        if(attributes.containsKey(MISSED_CONFIRMATIONS_ATTR))
        {
            currentCount = Integer.parseInt(attributes.get(MISSED_CONFIRMATIONS_ATTR).getFirst());
        }

        currentCount++;
        attributes.put(MISSED_CONFIRMATIONS_ATTR, List.of(String.valueOf(currentCount)));
        user.setAttributes(attributes);
        userResource.update(user);
        if(currentCount >= MAX_MISSED_CONFIRMATIONS)
        {
            suspendUser(userId);
        }
    }

    public void resetMissedConfirmations(String userId){
        UserResource userResource = keycloak.realm(realm).users().get(userId);
        UserRepresentation user = userResource.toRepresentation();

        Map<String, List<String>> attributes = user.getAttributes();
        if(attributes != null && attributes.containsKey(MISSED_CONFIRMATIONS_ATTR))
        {
            attributes.remove(MISSED_CONFIRMATIONS_ATTR);
            user.setAttributes(attributes);
            userResource.update(user);
        }
    }

    public int getMissedConfirmationsCount(String userId)
    {
        UserResource userResource = keycloak.realm(realm).users().get(userId);
        UserRepresentation user = userResource.toRepresentation();
        Map<String, List<String>> attributes = user.getAttributes();
        if(attributes != null && attributes.containsKey(MISSED_CONFIRMATIONS_ATTR))
        {
            return Integer.parseInt(attributes.get(MISSED_CONFIRMATIONS_ATTR).getFirst());
        }
        return 0;
    }

    private void suspendUser(String userId)
    {
        UserResource userResource = keycloak.realm(realm).users().get(userId);
        UserRepresentation user = userResource.toRepresentation();
        user.setEnabled(false);
        Map<String, List<String>> attributes = user.getAttributes();
        int numberOfSuspensions = 0;
        if(attributes == null)
        {
            attributes = new HashMap<>();
        }
        attributes.put("dateOfSuspension", List.of(LocalDateTime.now().toString()));
        attributes.put("dateToReactivate", List.of(LocalDateTime.now().plusDays(10).toString()));
        if(attributes.containsKey("nSuspensions"))
        {
            numberOfSuspensions = Integer.parseInt(attributes.get("nSuspensions").getFirst());

        }
        numberOfSuspensions++;
        attributes.put("nSuspensions", List.of(String.valueOf(numberOfSuspensions)));
        user.setAttributes(attributes);
        userResource.update(user);
    }

    //secondi minuti ore giorno mese giornosettimana
    @Scheduled(cron = "0 0 0 * * ?")
    private void checkForUserReactivation()
    {
        log.info("Executing reactivateUser at midnight...");
        List<String> usersToReactivate = new ArrayList<>();
        List<UserRepresentation> users = keycloak.realm(realm).users().list();
        for(UserRepresentation user : users)
        {
            if(!user.isEnabled())
            {
                usersToReactivate.add(user.getId());
            }
        }
        reactivateUser(usersToReactivate);
    }


    public void reactivateUser(List<String> usersToReactivate)
    {
        for(String userId : usersToReactivate)
        {
            UserResource userResource = keycloak.realm(realm).users().get(userId);
            UserRepresentation user = userResource.toRepresentation();
            Map<String, List<String>> attributes = user.getAttributes();
            if(attributes.containsKey("dateToReactivate"))
            {
                LocalDateTime date = LocalDateTime.parse(attributes.get("dateToReactivate").getFirst());
                if(date.isBefore(LocalDateTime.now()))
                {
                    user.setEnabled(true);
                    attributes.remove("dateToReactivate");
                    attributes.remove("dateOfSuspension");
                }
            }
            user.setEnabled(true);
            userResource.update(user);
            resetMissedConfirmations(userId);
        }
    }

    public String getUserEmail(String userId)
    {
        UserResource userResource = keycloak.realm(realm).users().get(userId);
        UserRepresentation user = userResource.toRepresentation();
        Map<String, List<String>> attributes = user.getAttributes();
        String email = attributes.get("email").getFirst();
        return email;
    }

    public Map getAttributes(String userId)
    {
        UserResource userResource = keycloak.realm(realm).users().get(userId);
        log.info("\n UserResource: {}", userResource);
        UserRepresentation user = userResource.toRepresentation();
        log.info("\n UserRepresentation: {}", user);
        Map<String, List<String>> attributes = user.getAttributes();
        log.info("\n Attributes: {}", attributes);
        return attributes;
    }

}
