/**
 * Copyright (C) 2015 Open Whisper Systems
 * Copyright (C) 2018 Sinan Turgut
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package tr.com.sinanturgut.fcm.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tr.com.sinanturgut.fcm.server.internal.FcmRequestEntity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Message {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private final String              collapseKey;
  private final Long                ttl;
  private final Boolean             delayWhileIdle;
  private final Map<String, String> data;
  private final List<String>        registrationIds;
  private final String              priority;

  private Message(String collapseKey, Long ttl, Boolean delayWhileIdle,
                  Map<String, String> data, List<String> registrationIds,
                  String priority)
  {
    this.collapseKey     = collapseKey;
    this.ttl             = ttl;
    this.delayWhileIdle  = delayWhileIdle;
    this.data            = data;
    this.registrationIds = registrationIds;
    this.priority        = priority;
  }

  public String serialize() throws JsonProcessingException {
    FcmRequestEntity requestEntity = new FcmRequestEntity(collapseKey, ttl, delayWhileIdle,
                                                          data, registrationIds, priority);

    return objectMapper.writeValueAsString(requestEntity);
  }

  /**
   * Construct a new Message using a Builder.
   * @return A new Builder.
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {

    private String              collapseKey     = null;
    private Long                ttl             = null;
    private Boolean             delayWhileIdle  = null;
    private Map<String, String> data            = null;
    private List<String>        registrationIds = new LinkedList<>();
    private String              priority        = null;

    private Builder() {}

    /**
     * @param collapseKey The FCM collapse key to use (optional).
     * @return The Builder.
     */
    public Builder withCollapseKey(String collapseKey) {
      this.collapseKey = collapseKey;
      return this;
    }

    /**
     * @param seconds The TTL (in seconds) for this message (optional).
     * @return The Builder.
     */
    public Builder withTtl(long seconds) {
      this.ttl = seconds;
      return this;
    }

    /**
     * @param delayWhileIdle Set FCM delay_while_idle (optional).
     * @return The Builder.
     */
    public Builder withDelayWhileIdle(boolean delayWhileIdle) {
      this.delayWhileIdle = delayWhileIdle;
      return this;
    }

    /**
     * Set a key in the FCM JSON payload delivered to the application (optional).
     * @param key The key to set.
     * @param value The value to set.
     * @return The Builder.
     */
    public Builder withDataPart(String key, String value) {
      if (data == null) {
        data = new HashMap<>();
      }
      data.put(key, value);
      return this;
    }

    /**
     * Set the destination FCM registration ID (mandatory).
     * @param registrationId The destination FCM registration ID.
     * @return The Builder.
     */
    public Builder withDestination(String registrationId) {
      this.registrationIds.clear();
      this.registrationIds.add(registrationId);
      return this;
    }

    /**
     * Set the FCM message priority (optional).
     *
     * @param priority Valid values are "normal" and "high."
     *                 On iOS, these correspond to APNs priority 5 and 10.
     * @return The Builder.
     */
    public Builder withPriority(String priority) {
      this.priority = priority;
      return this;
    }

    /**
     * Construct a message object.
     *
     * @return An immutable message object, as configured by this builder.
     */
    public Message build() {
      if (registrationIds.isEmpty()) {
        throw new IllegalArgumentException("You must specify a destination!");
      }

      return new Message(collapseKey, ttl, delayWhileIdle, data, registrationIds, priority);
    }
  }


}
