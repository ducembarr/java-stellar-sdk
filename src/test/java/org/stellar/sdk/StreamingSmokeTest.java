package org.stellar.sdk;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.stellar.sdk.requests.EventListener;
import org.stellar.sdk.requests.SSEStream;
import org.stellar.sdk.responses.TradeResponse;

import java.util.concurrent.atomic.AtomicInteger;

public class StreamingSmokeTest {

  @Test
  @Ignore // lets not run this by default for now
  public void shouldStreamPaymentsFromTestNet() {
    final AtomicInteger events = new AtomicInteger();
    Server server = new Server("https://horizon-testnet.stellar.org/");
    Network.useTestNetwork();
    SSEStream<TradeResponse> manager = null;
    try {
        manager = server.trades().limit(100).stream(new EventListener<TradeResponse>() {
          @Override
          public void onEvent(TradeResponse r) {
            events.incrementAndGet();
          }
        });

        try {
          Thread.sleep(30000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        manager.close();
      int eventCount = events.get();
      Assert.assertTrue(eventCount >0);

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      Assert.assertTrue(events.get() == eventCount);

    } finally {
      if(manager != null) {
        manager.close();
      }
    }
  }
}
