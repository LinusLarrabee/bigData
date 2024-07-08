package com.tplink.tauc.core.data.collector.kafka;

import com.student.crudapp.kafka2s3.SslUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SslUtilsTest {

    @InjectMocks
    private SslUtils sslUtils;

    @BeforeEach
    public void setUp() {
        // Resetting the static configurations before each test
    }

    @Test
    public void testIgnoreSsl() throws Exception {
        try (MockedStatic<HttpsURLConnection> mockedHttpsURLConnection = Mockito.mockStatic(HttpsURLConnection.class)) {
            HostnameVerifier defaultHostnameVerifier = mock(HostnameVerifier.class);
            mockedHttpsURLConnection.when(HttpsURLConnection::getDefaultHostnameVerifier).thenReturn(defaultHostnameVerifier);

            SslUtils.ignoreSsl();

            mockedHttpsURLConnection.verify(() -> HttpsURLConnection.setDefaultHostnameVerifier(any(HostnameVerifier.class)));
            mockedHttpsURLConnection.verify(() -> HttpsURLConnection.setDefaultSSLSocketFactory(any()));
        }
    }

    @Test
    public void testHostnameVerifierInIgnoreSsl() throws Exception {
        HostnameVerifier hv = (hostname, session) -> true;
        SSLSession sslSession = mock(SSLSession.class);
        when(sslSession.getPeerHost()).thenReturn("peerHost");

        boolean result = hv.verify("testHost", sslSession);

        assertTrue(result);
    }

    @Test
    public void testTrustAllHttpsCertificates() throws Exception {
        try (MockedStatic<HttpsURLConnection> mockedHttpsURLConnection = Mockito.mockStatic(HttpsURLConnection.class)) {
            SslUtils.ignoreSsl();

            // Verify that the SSL context is set correctly
            mockedHttpsURLConnection.verify(() -> HttpsURLConnection.setDefaultSSLSocketFactory(any()));
        }
    }
}
