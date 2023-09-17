Certainly, let's set up the `opensearchjobstarter` in detail. 

The aim of this starter is to provide generic capabilities for reading from OpenSearch and writing the results elsewhere, without the knowledge of the specific use-case. Therefore, it will rely heavily on external configuration and parameters at runtime.

### **opensearchjobstarter Project**

### 1. OpenSearchConfig
This class provides the necessary configurations to interact with OpenSearch.

```java
package com.yourcompany.starter.opensearch.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenSearchConfig {

    @Value("#{jobParameters['host']}")
    private String host;

    @Value("#{jobParameters['port']}")
    private int port;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, "http")));
    }
}
```

### 2. OpenSearchItemReader
This reader is designed to extract data from OpenSearch using the provided parameters.

```java
package com.yourcompany.starter.opensearch.batch;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.StepScope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@StepScope
public class OpenSearchItemReader implements ItemReader<String> {

    @Autowired
    private RestHighLevelClient client;

    @Value("#{jobParameters['query']}")
    private String query;

    @Value("#{jobParameters['indexName']}")
    private String indexName;

    private int currentCount = 0;
    private int totalRecords = -1;

    public OpenSearchItemReader() throws IOException {
        CountRequest countRequest = new CountRequest(indexName);
        countRequest.source(new SearchSourceBuilder().query(QueryBuilders.wrapperQuery(query)));
        totalRecords = (int) client.count(countRequest, RequestOptions.DEFAULT).getCount();
    }

    @Override
    public String read() throws Exception {
        if (currentCount < totalRecords) {
            // Use client to retrieve data
            // Increment currentCount accordingly
        } else {
            return null;
        }
    }
}
```

### 3. spring.factories
This file helps in auto-configuring your starter for any Spring Boot application that adds it as a dependency.

#### Location:
`resources/META-INF/spring.factories`

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.yourcompany.starter.opensearch.config.OpenSearchConfig,\
com.yourcompany.starter.opensearch.batch.OpenSearchItemReader
```

---

### Usage in SensAIBulkSearch

When you integrate the `opensearchjobstarter` into your `SensAIBulkSearch` application, you'll simply need to provide the necessary job parameters at runtime (host, port, index name, query) when initiating the Spring Batch job. These parameters can come from the Kafka message (parsed in the `filejobstarter`) and the other configuration sources you have in the `SensAIBulkSearch` application.

The `opensearchjobstarter` will handle the interaction with OpenSearch based on the provided parameters. The data fetched from OpenSearch can then be processed and written as per the logic defined in the `SensAIBulkSearch` application.

Remember, since the reader in the `opensearchjobstarter` is designed to read one record at a time (to be memory-efficient), the actual implementation needs to handle paginating through OpenSearch results properly.