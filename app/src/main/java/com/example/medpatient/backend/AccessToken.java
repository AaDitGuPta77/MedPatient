package com.example.medpatient.backend;

import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class AccessToken {

    private static final String firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging";

    public String getAccessToken() {
        try {
            String jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"mediquick-f9cad\",\n" +
                    "  \"private_key_id\": \"86b1479888c0d562f1d8bf3039f5a540b69f2ce3\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQD4dY4VMZrtos5e\\npfQ8ym+GC1YYggVa4WOMqqOyTf55j66K4ZqhKx3aN8cHTQpeh5L/X4XAYQd8HLlm\\ndPZSpOhqxILq0Kk2nP+g3tLyThtCU9/wpgCkh51/a/ZEtY6+LhXEqycL2MCFfNJD\\ngE6Y5Jnfcs7r86qzj/kYAMO1LNNtw8dNI3VUJGlOwuX6HahJzeNoZ8nCqhQDZk2Z\\nCd7wSaegTdhJKcCCa/Mou/wsO2C/t2ZOvVVOfXtxyoi7MZ79r6tKBLFvCq0eRVvA\\n7ZskrNk5tQLNIO73Vsw+VjgWhbKqmVTeqzAlvTvOuI9BPvf0LojubQe1lcb3qUZv\\nqo0+H2hpAgMBAAECggEAM0iWa275/s9iP9yKyWpycw1Pd88MmmnX0a7eC314R3Ce\\nExoGf/pFlud47Budho3+zDnUAnW0pQxLw2dkeYIC3BjUOfIYX0awJ+0eRvfSvhHQ\\ncRxemRdJ7cVjx2JfRIeAJEA8lsAmWtc9EkQ/VOUokpvgtIiuxRH0VR1BSBnoySie\\nEotdy466z9Wy4wa6y0k1rAKe1vHCuALtMSd23UTaqZDAGRKZBCZPkuXiTiyLytC2\\nsJVwOJ3l2/t+5EtKlYNoEDaO+e+Nr9rYxh2jbaxDVDuU/FVI2mqPAbTYFOYeFl6U\\nTWYsrPBlgjAbUCWqR1b5YvlcbzTG2Bpe7ujldl1qRQKBgQD+IXQDD+jNHYBYyiW8\\nV5tA5BLjxArigLhGKJsHA0urpdV8I11QaGpOc1/PY1+XEnztgrRv5ApyJsUYBPpy\\naehOSWg2QLtLLdN+1RAhQ0cUDxivc/iv0Xw8T0Ly2HPIJ3tT+f2p+B88DqHX2tsr\\nZ5H6ok7yJjpIi/OHxn/6sgHGxQKBgQD6SWwKFRmqkvMLXS1whyePVlWN3ZFe2Vtx\\nOOx3Nz54wqYDSi62ZwO5wh7x20U6TievKFGlifnLlh4RidQpfdJM4/KIgQjOGckI\\nYMd9Sl1mkjOmCXIbQ0FKw3rs6BJ0p1rfKj7l5Iwcg672HKBSYpWWVivc+ULX525l\\n0WOuKvVVVQKBgQCjHjHq7UFOt0r0svw5wjnDEyIU1HbwIBMn5VRs0e62RIGdZs20\\nmPDzXhb3dR+cRp+tjCHpWARkdQI5gqiG2/fimDet6yrM/KMuW/A4iYiMFYyQwe20\\nXNBTncSpP8pBhSYiJuptOPhDgljbxny7hYUrzRbDU0pB2qL8Was37R46yQKBgE6U\\nfrcrqMNFH8mRl5Kc3G7ETFgeGGYPGAaUOKm1TofblQOzZrcd4K2RpOWKUlHb99mm\\nbvvqNeMVXptjCpl8R2qcpqj02i7bKaLXv8o2btylSlH25TxqATXX6eA3fJPjd0RZ\\nnyXOe674KRZcJnm3Of/ZTiwylinzH1YYR5OHKT+BAoGAIwvH6srhRRhBC8vyXMCA\\ne90MeoqpfqvuweOPImZLpMlO44iL0rWu9EcqkeqcR6k6Mc8qweEMqKvuMvCSerPO\\nYaHMbN+KJfaIgf+WOyO7lTVMd/OePN6r+GM9ZKZgF/FLR/N477DDk62qbTce5AYi\\n36BWRJBt9ighwAtI0XhVwps=\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-fbsvc@mediquick-f9cad.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"100919527862277343925\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-fbsvc%40mediquick-f9cad.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}\n";

            InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));

            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(inputStream).createScoped(Arrays.asList(firebaseMessagingScope));

            googleCredentials.refresh();

            return googleCredentials.getAccessToken().getTokenValue();
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return null;
        }
    }
}
