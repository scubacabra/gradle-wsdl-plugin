
package helloworld.sample.ibm.com;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import helloworld.sample.ibm.helloworld.HelloResponseType;
import helloworld.sample.ibm.helloworld.HelloType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the helloworld.sample.ibm.com package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Hello_QNAME = new QName("urn:helloWorld/sample/ibm/com", "hello");
    private final static QName _HelloResponse_QNAME = new QName("urn:helloWorld/sample/ibm/com", "helloResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: helloworld.sample.ibm.com
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HelloType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:helloWorld/sample/ibm/com", name = "hello")
    public JAXBElement<HelloType> createHello(HelloType value) {
        return new JAXBElement<HelloType>(_Hello_QNAME, HelloType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HelloResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:helloWorld/sample/ibm/com", name = "helloResponse")
    public JAXBElement<HelloResponseType> createHelloResponse(HelloResponseType value) {
        return new JAXBElement<HelloResponseType>(_HelloResponse_QNAME, HelloResponseType.class, null, value);
    }

}
