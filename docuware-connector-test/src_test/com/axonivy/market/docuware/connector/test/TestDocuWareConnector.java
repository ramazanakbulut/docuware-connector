package com.axonivy.market.docuware.connector.test;

import com.axonivy.connector.docuware.connector.DocuWareAuthFeature;
import com.axonivy.connector.docuware.connector.DocuWareEndpointConfiguration;
import com.axonivy.connector.docuware.connector.DocuWareProperty;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.bpm.exec.client.IvyProcessTest;
import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.rest.client.RestClient;
import ch.ivyteam.ivy.rest.client.RestClient.Builder;
import ch.ivyteam.ivy.rest.client.RestClients;
import ch.ivyteam.ivy.scripting.objects.List;

@IvyProcessTest
public class TestDocuWareConnector {


	protected void prepareRestClient(IApplication app, AppFixture fixture) {

		fixture.var("docuware-connector.host", "TESTHOST");
		fixture.var("docuware-connector.username", "TESTUSER");
		fixture.var("docuware-connector.password", "TESTPASS");
		fixture.var("docuware-connector.hostid", "TESTHOSTID");
		fixture.var("docuware-connector.filecabinetid", "123");


		RestClient restClient = RestClients.of(app).find("DocuWare");

		// change created client: use test url and a slightly different version of the DocuWare Auth feature
		Builder builder = RestClient
				.create(restClient.name())
				.uuid(restClient.uniqueId())
				.uri("http://{ivy.engine.host}:{ivy.engine.http.port}/{ivy.request.application}/api/docuWareMock")
				.description(restClient.description())
				.properties(restClient.properties());

		// use test feature instead of real one
		for(String feature : restClient.features()) {
			if(feature.contains(DocuWareAuthFeature.class.getCanonicalName())) {
				feature = DocuWareAuthTestFeature.class.getCanonicalName();
			}
			
			
			builder.feature(feature);
		}
		builder.feature("ch.ivyteam.ivy.rest.client.security.CsrfHeaderFeature");
		restClient = builder.toRestClient();
		RestClients.of(app).set(restClient);
	}
	
	protected List<DocuWareProperty> prepareDocuWareProperties(){
		List<DocuWareProperty> propertyList = new List<DocuWareProperty>();
		DocuWareProperty dwp = new DocuWareProperty();
		dwp.setFieldName("ACCESS_LEVEL");
		dwp.setItem("3");
		dwp.setItemElementName("String");
		propertyList.add(dwp);
		return propertyList;
	}
	
	protected DocuWareEndpointConfiguration prepareDocuWareEndpointConfiguration(){
		DocuWareEndpointConfiguration configuration = new DocuWareEndpointConfiguration();
		return configuration;
	}
}
