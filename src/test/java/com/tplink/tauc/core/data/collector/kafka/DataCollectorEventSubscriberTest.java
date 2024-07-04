package com.tplink.tauc.core.data.collector.kafka;

import com.tplink.nbu.common.utils.JacksonUtil;
import com.tplink.smb.eventcenter.api.Event;
import com.tplink.smb.eventcenter.api.EventCenter;
import com.tplink.tauc.core.data.collector.DTO.DataCollectorDTO;
import com.tplink.tauc.core.data.collector.DTO.DataCollectorEventDTO;
import com.tplink.tauc.core.data.collector.DTO.DataReceiverResp;
import com.tplink.tauc.core.data.collector.config.CollectorProps;
import com.tplink.tauc.core.data.collector.config.EmailProps;
import com.tplink.tauc.core.data.collector.service.DataCollectorService;
import com.tplink.tauc.core.data.collector.service.SendEmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import javax.net.ssl.SSLSession;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@PrepareForTest({Event.class, JacksonUtil.class})
public class DataCollectorEventSubscriberTest {

    @Mock
    private EventCenter eventCenter;

    @Mock
    private DataCollectorService dataCollectorService;

    @Mock
    private CollectorProps collectorProps;

    @Mock
    private SendEmailService sendEmailService;

    @Mock
    private EmailProps emailProps;

    @InjectMocks
    private DataCollectorEventSubscriber dataCollectorEventSubscriber;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleEvent() throws Exception {
        // Prepare the mock event and its behavior
        Event event = PowerMockito.mock(Event.class);
        when(event.getFilterKey()).thenReturn(DataCollectorEventSubscriber.DATA_COLLECTOR_EVENT_FILTER_KEY);

        DataCollectorEventDTO dataCollectorEventDTO = new DataCollectorEventDTO();
        DataCollectorDTO dataCollectorDTO = new DataCollectorDTO();
        dataCollectorEventDTO.setDataCollectorDTO(dataCollectorDTO);
        dataCollectorEventDTO.setId("testId");

        PowerMockito.mockStatic(JacksonUtil.class);
        when(JacksonUtil.convertTo(event.getMessage(), DataCollectorEventDTO.class)).thenReturn(dataCollectorEventDTO);

        DataReceiverResp dataReceiverResp = new DataReceiverResp();
        dataReceiverResp.setError_code(1);
        when(dataCollectorService.Connector(dataCollectorDTO, "testId")).thenReturn(dataReceiverResp);

        // Call the method under test
        dataCollectorEventSubscriber.handleEvent(event);

        // Verify interactions and behavior
        verify(dataCollectorService, times(1)).Connector(dataCollectorDTO, "testId");
        verify(sendEmailService, never()).sendEmail(anyString(), anyList());
    }
}
