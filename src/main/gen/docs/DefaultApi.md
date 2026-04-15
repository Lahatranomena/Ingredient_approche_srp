# DefaultApi

All URIs are relative to *http://localhost:3000/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**ingredientsGet**](DefaultApi.md#ingredientsGet) | **GET** /ingredients | Liste des ingrédients |


<a id="ingredientsGet"></a>
# **ingredientsGet**
> List&lt;Ingredient&gt; ingredientsGet()

Liste des ingrédients

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:3000/v1");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    try {
      List<Ingredient> result = apiInstance.ingredientsGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#ingredientsGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;Ingredient&gt;**](Ingredient.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Retourne la liste des ingrédients enregistrés |  -  |
| **500** | Erreur interne du serveur |  -  |

