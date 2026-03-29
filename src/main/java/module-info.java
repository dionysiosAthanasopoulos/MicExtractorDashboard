module MicExtractorDashboard {

	requires javafx.controls;

	requires javafx.fxml;

	requires java.desktop;

	requires javafx.graphics;

	requires org.apache.httpcomponents.httpclient;

	requires org.apache.httpcomponents.httpcore;

	requires javafx.base;

	requires com.fasterxml.jackson.annotation;

	requires com.fasterxml.jackson.databind;

	requires java.xml.bind;

	requires com.fasterxml.jackson.core;

	opens MicGenAI.MicExtractor.dashboard to javafx.fxml, java.xml.bind;

	exports MicGenAI.MicExtractor.dashboard;

	exports httpClients;
}