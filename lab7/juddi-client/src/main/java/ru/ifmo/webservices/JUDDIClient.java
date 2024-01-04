package ru.ifmo.webservices;

import org.apache.juddi.api_v3.AccessPointType;
import org.apache.juddi.v3.client.UDDIConstants;
import org.apache.juddi.v3.client.config.UDDIClient;
import org.apache.juddi.v3.client.transport.Transport;
import org.uddi.api_v3.*;
import org.uddi.v3_service.UDDIInquiryPortType;
import org.uddi.v3_service.UDDIPublicationPortType;
import org.uddi.v3_service.UDDISecurityPortType;

import java.rmi.RemoteException;

public class JUDDIClient {
    private static UDDISecurityPortType security = null;
    private static UDDIInquiryPortType inquiry = null;
    private static UDDIPublicationPortType publish = null;

    public JUDDIClient() {
        try {
            UDDIClient client = new UDDIClient("META-INF/uddi.xml");
            Transport transport = client.getTransport("default");

            security = transport.getUDDISecurityService();
            inquiry = transport.getUDDIInquiryService();
            publish = transport.getUDDIPublishService();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getAuthKey(String username, String password) {
        GetAuthToken getAuthToken = new GetAuthToken();
        getAuthToken.setUserID(username);
        getAuthToken.setCred(password);

        try {
            AuthToken rootAuthToken = security.getAuthToken(getAuthToken);
            return rootAuthToken.getAuthInfo();
        } catch (Exception e) {
            System.out.println("Could not authenticate with the provided credentials " + e.getMessage());
        }
        return null;
    }

    public void publishService(String authToken, String businessName, String serviceName, String serviceDescription, String accessPointPath) {
        try {
            // Creating the parent business entity that will contain our service.
            BusinessEntity businessEntity = new BusinessEntity();

            Name myBusName = new Name();
            myBusName.setValue(businessName);
            businessEntity.getName().add(myBusName);

            // Adding the business entity to the "save" structure, using our publisher's authentication info and saving away.
            SaveBusiness saveBusiness = new SaveBusiness();
            saveBusiness.getBusinessEntity().add(businessEntity);
            saveBusiness.setAuthInfo(authToken);
            BusinessDetail businessDetail = publish.saveBusiness(saveBusiness);
            String businessKey = businessDetail.getBusinessEntity().get(0).getBusinessKey();

            // Creating a service to save. Only adding the minimum data: the parent business key retrieved from saving the business above and a single name.
            BusinessService businessService = new BusinessService();
            businessService.setBusinessKey(businessKey);

            Name myServName = new Name();
            myServName.setValue(serviceName);
            businessService.getName().add(myServName);

            Description myServDescription = new Description();
            myServDescription.setValue(serviceDescription);
            businessService.getDescription().add(myServDescription);

            // Add binding templates, etc...
            BindingTemplate bindingTemplate = new BindingTemplate();
            AccessPoint accessPoint = new AccessPoint();
            accessPoint.setUseType(AccessPointType.WSDL_DEPLOYMENT.toString());
            accessPoint.setValue(accessPointPath);
            bindingTemplate.setAccessPoint(accessPoint);
            BindingTemplates bindingTemplates = new BindingTemplates();
            bindingTemplate = UDDIClient.addSOAPtModels(bindingTemplate);
            bindingTemplates.getBindingTemplate().add(bindingTemplate);
            businessService.setBindingTemplates(bindingTemplates);

            // Adding the service to the "save" structure, using our publisher's authentication info and saving away.
            SaveService saveService = new SaveService();
            saveService.getBusinessService().add(businessService);
            saveService.setAuthInfo(authToken);
            publish.saveService(saveService);

            System.out.println("Service is successfully published!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BusinessService findServiceByName(String authToken, String serviceName) {
        FindService findService = new FindService();
        findService.setAuthInfo(authToken);
        findService.setFindQualifiers(new FindQualifiers());
        findService.getFindQualifiers().getFindQualifier().add(UDDIConstants.EXACT_MATCH);

        Name searchName = new Name();
        searchName.setValue(serviceName);
        findService.getName().add(searchName);

        try {
            ServiceList serviceList = inquiry.findService(findService);
            if (serviceList.getServiceInfos() == null) {
                return null;
            }
            ServiceInfo serviceInfo = serviceList.getServiceInfos().getServiceInfo().get(0);

            GetServiceDetail getServiceDetail = new GetServiceDetail();
            getServiceDetail.getServiceKey().add(serviceInfo.getServiceKey());
            ServiceDetail serviceDetail = inquiry.getServiceDetail(getServiceDetail);

            return serviceDetail.getBusinessService().get(0);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
