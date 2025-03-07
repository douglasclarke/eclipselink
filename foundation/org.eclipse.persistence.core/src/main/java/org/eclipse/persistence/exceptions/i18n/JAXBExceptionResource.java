/*
 * Copyright (c) 1998, 2021 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 1998, 2018 IBM Corporation and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */

// Contributors:
// mmacivor - June 11/2008 - 1.0 - Initial implementation
//     09/24/2014-2.6 Rick Curtis
//       - 443762 : Misc message cleanup.
//     12/18/2014-2.6 Rick Curtis
//       - 454189 : Misc message cleanup.#2
package org.eclipse.persistence.exceptions.i18n;

import java.util.ListResourceBundle;

/**
 * INTERNAL:
 * <b>Purpose:</b><p>English ResourceBundle for JAXBException.</p>
 */
public final class JAXBExceptionResource extends ListResourceBundle {
    static final Object[][] contents = {
        {"50000", "The context path {0} contains no ObjectFactory or jaxb.index, no external metadata was found in properties Map, and sessions.xml was found or was invalid."},
        {"50001", "The class {0} requires a zero argument constructor or a specified factory method.  Note that non-static inner classes do not have zero argument constructors and are not supported."},
        {"50002", "Factory class specified without factory method on class {0}."},
        {"50003", "The factory method named {0} is not declared on the class {1}."},
        {"50004", "XmlAnyAttribute is invalid on property {0}. Must be used with a property of type Map."},
        {"50005", "Only one property with XmlAnyAttribute allowed on class {0}."},
        {"50006", "Invalid XmlElementRef on property {0} on class {1}. Referenced Element not declared."},
        {"50007", "Name collision.  Two classes have the XML type with uri {0} and name {1}."},
        {"50008", "Unsupported Node class {0}.  The createBinder(Class) method only supports the class org.w3c.dom.Node."},
        {"50009", "The property or field {0} on the class {1} is annotated to be transient so it cannot be included in the propOrder annotation."},
        {"50010", "The property or field {0} on the class {1} must be an attribute because another field or property is annotated with XmlValue."},
        {"50011", "The property or field {0} on the class {1} cannot be annotated with XmlValue since it is a subclass of another XML-bound class."},
        {"50012", "The property or field {0} on the class {1} was specified in propOrder but is not a valid property."},
        {"50013", "The property or field {0} on the class {1} is required to be included in the propOrder element of the XmlType annotation."},
        {"50014", "The property or field {0} on the class {1} with the XmlValue annotation must be of a type that maps to a simple schema type."},
        {"50015", "XmlElementWrapper is only allowed on a collection or array property but [{0}] is not a collection or array property."},
        {"50016", "Property [{0}] has an XmlID annotation but its type is not String."},
        {"50017", "Invalid XmlIDREF on property [{0}].  Class [{1}] is required to have a property annotated with XmlID."},
        {"50018", "XmlList is only allowed on a collection or array property but [{0}] is not a collection or array property."},
        {"50019", "Invalid parameter type encountered while processing external metadata via properties Map.  The Value associated with Key [eclipselink-oxm-xml] is required to be one of [Map<String, Object>, List<Object>, or Object].  Object must be one of [java.io.File, java.io.InputStream, java.io.Reader, java.net.URL, javax.xml.stream.XMLEventReader, javax.xml.stream.XMLStreamReader, javax.xml.transform.Source, org.w3c.dom.Node, or org.xml.sax.InputSource], In the case of [Map<String, Object>], String is the package name."},
        {"50021", "Invalid parameter type encountered while processing external metadata via properties Map.  For [Map<String, Object>], it is required that the Key be of type [String] (indicating package name)."},
        {"50022", "Invalid parameter type encountered while processing external metadata via properties Map.  For [Map<String, Object>], it is required that the Value be one of [java.io.File, java.io.InputStream, java.io.Reader, java.net.URL, javax.xml.stream.XMLEventReader, javax.xml.stream.XMLStreamReader, javax.xml.transform.Source, org.w3c.dom.Node, or org.xml.sax.InputSource] (handle to metadata file)."},
        {"50023", "A null Value for Key [{0}] was encountered while processing external metadata via properties Map.  It is required that the Value be non-null and one of one of [java.io.File, java.io.InputStream, java.io.Reader, java.net.URL, javax.xml.stream.XMLEventReader, javax.xml.stream.XMLStreamReader, javax.xml.transform.Source, org.w3c.dom.Node, or org.xml.sax.InputSource] (handle to metadata file)."},
        {"50024", "A null Key was encountered while processing external metadata via properties Map.  It is required that the Key be non-null and of type [String] (indicating package name)."},
        {"50025", "Could not load class [{0}] declared in the external metadata file.  Please ensure that the class name is correct, and that the correct ClassLoader has been set."},
        {"50026", "An exception occurred while attempting to create a JAXBContext for the XmlModel."},
        {"50027", "An exception occurred while attempting to unmarshal externalized metadata file."},
        {"50028", "A new instance of [{0}] could not be created."},
        {"50029", "The class [{0}] provided on the XmlCustomizer does not implement the org.eclipse.persistence.config.DescriptorCustomizer interface."},
        {"50030", "An attempt was made to set more than one XmlID property on class [{1}].  Property [{0}] cannot be set as XmlID, because property [{2}] is already set as XmlID."},
        {"50031", "An attempt was made to set more than one XmlValue property on class [{0}].  Property [{1}] cannot be set as XmlValue, because property [{2}] is already set as XmlValue."},
        {"50032", "An attempt was made to set more than one XmlAnyElement property on class [{0}].  Property [{1}] cannot be set as XmlAnyElement, because property [{2}] is already set as XmlAnyElement."},
        {"50033", "The DomHandlerConverter for DomHandler [{0}] set on property [{1}] could not be initialized."},
        {"50034", "The property or field [{0}] cannot be annotated with XmlAttachmentRef since it is not a DataHandler."},
        {"50035", "Since the property or field [{0}] is set as XmlIDREF, the target type of each XmlElement declared within the XmlElements list must have an XmlID property.  Please ensure the target type of XmlElement [{1}] contains an XmlID property."},
        {"50036", "The TypeMappingInfo with XmlTagName QName [{0}] needs to have a non-null Type set on it."},
        {"50037", "The java-type with package [{0}] is not allowed in the bindings file keyed on package [{1}]."},
        {"50038", "DynamicJAXBContext cannot be created from concrete Classes.  Please use org.eclipse.persistence.jaxb.JAXBContext, or specify org.eclipse.persistence.jaxb.JAXBContextFactory in your jaxb.properties file, to create a context from existing Classes."},
        {"50039", "Error creating DynamicJAXBContext: Node must be an instance of either Document or Element."},
        {"50040", "Error creating DynamicJAXBContext."},
        {"50041", "Enum constant [{0}] not found."},
        {"50042", "Error creating DynamicJAXBContext: Session name was null."},
        {"50043", "Error creating DynamicJAXBContext: Source was null."},
        {"50044", "Error creating DynamicJAXBContext: InputStream was null."},
        {"50045", "Error creating DynamicJAXBContext: Node was null."},
        {"50046", "Error creating DynamicJAXBContext: XJC was unable to generate a CodeModel."},
        {"50047", "Class [{0}] not found."},
        {"50048", "The read transformer specified for property [{0}] has both class and method. Either class or method is required, but not both."},
        {"50049", "The read transformer specified for property [{0}] has neither class nor method. A class or method is required."},
        {"50050", "The write transformer specified for the xml-path  [{1}] of property [{0}] has both class and method. Either class or method is required, but not both."},
        {"50051", "The write transformer specified for the xml-path  [{1}] of property [{0}] has neither class nor method. A class or method is required."},
        {"50052", "The write transformer specified for property [{0}] does not have an xml-path set. An xml-path is required."},
        {"50053", "The transformation method [{0}] with parameters (), (AbstractSession) or (Session) not found."},
        {"50054", "Transformer class [{0}] not found. Please ensure that the class name is correct, and that the correct ClassLoader has been set."},
        {"50055", "Error creating DynamicJAXBContext: eclipselink.oxm.metadata-source (JAXBContextProperties.OXM_METADATA_SOURCE) not found in properties map, or map was null."},
        {"50056", "Property [{0}] contains an XmlJoinNode declaration, but the referenced class [{1}] is not applicable for this type of relationship."},
        {"50057", "Property [{1}] in class [{0}] references a class [{2}] that is marked transient, which is not allowed."},
        {"50058", "Property [{1}] in class [{0}] has an XmlJoinNode declaration, but the target class [{2}] has no XmlID property or XmlKey properties.  It is required that there is an XmlID/XmlKey property with a matching XmlPath on the target class for each referencedXmlPath."},
        {"50059", "Property [{1}] in class [{0}] has an XmlJoinNode declaration with referencedXmlPath [{3}], but there is no XmlID or XmlKey property on the target class [{2}] with the XmlPath [{3}].  It is required that there is an XmlID/XmlKey property with a matching XmlPath on the target class for each referencedXmlPath."},
        {"50060", "Property [{1}] in class [{0}] has an XmlIDREF declaration, but the target class [{2}] is not applicable for this type of relationship."},
        {"50061", "An exception occurred while attempting to load XmlAdapterClass [{0}]. Possible causes are an incorrect adapter class name or the wrong loader has been set."},
        {"50062", "An exception occurred while attempting to access the declared methods of XmlAdapterClass [{0}]. Possible causes are that the SecurityManager has denied access to the declared methods within the adapter class, or the SecuritManager has denied access to the package of the adapter class."},
        {"50063", "An exception occurred while attempting to instantiate XmlAdapterClass [{0}]. A possible cause is that the adapter class has no zero argument constructor."},
        {"50064", "XmlAdapterClass [{0}] does not extend \"jakarta.xml.bind.annotation.adapters.XmlAdapter\" as expected.  It is required that the adapter class extend \"jakarta.xml.bind.annotation.adapters.XmlAdapter\", and declare methods \"public abstract BoundType unmarshal(ValueType v)\" and \"public abstract ValueType marshal(BoundType v)\"."},
        {"50065", "An invalid XmlJavaTypeAdapter [{0}] was specified for package [{1}]. Possible causes are an incorrect adapter class name or the wrong loader has been set."},
        {"50066", "An invalid XmlJavaTypeAdapter [{0}] was specified for class [{1}]. Possible causes are an incorrect adapter class name or the wrong loader has been set."},
        {"50067", "An invalid XmlJavaTypeAdapter [{0}] was specified for field/property [{1}] on class [{2}]. Possible causes are an incorrect adapter class name or the wrong loader has been set."},
        {"50068", "A null value was encountered while processing external metadata via properties Map.  It is required that the handle to the XML metadata file be non-null and one of [java.io.File, java.io.InputStream, java.io.Reader, java.net.URL, javax.xml.stream.XMLEventReader, javax.xml.stream.XMLStreamReader, javax.xml.transform.Source, org.w3c.dom.Node, or org.xml.sax.InputSource]."},
        {"50069", "A package was not specified for the provided XML metadata file.  The package can be specified by passing in Map<String, Object> (where String = package, Object = handle to XML metadata file) or by setting the package-name attribute on the xml-bindings element in the XML metadata file."},
        {"50070", "Property [{0}] on class [{1}] has an XmlElements declaration containing an unequal amount of XmlElement/XmlJoinNodes.  It is required that there be a corresponding XmlJoinNodes for each XmlElement contained within the XmlElements declaration."},
        {"50071", "Property [{0}] on class [{1}] has an XmlPaths declaration containing an XmlPath with an attribute at the root of the path [{2}].  In the case of XmlPaths, attributes must be nested in the XmlPath, i.e. [foo/{2}]."},
        {"50072", "Duplicate Property named [{0}] found on class [{1}]"},
        {"50073", "Property [{0}] on class [{1}] is specified in multiple external bindings files. Each property can only be specified in one file"},
        {"50074", "An exception occurred accessing the XMLNameTransformer [{0}]"},
        {"50075", "An exception occurred while attempting to transform name [{0}] with the XMLNameTransformer [{1}]"},
        {"50076", "Unable to load external metadata from the provided location: [{0}]. This location must be either a valid URL or a classpath reference."},
        {"50077", "Cannot refresh metadata.  Metadata must be provided as an XML Node in order to support refreshing."},
        {"50078", "Cannot process external bindings files (XJB).  To use external bindings files, both XSD and XJB must be provided as javax.xml.transform.Sources."},
        {"50079", "Cannot process schemas.  If using schema imports, XSDs must be provided as a javax.xml.transform.Source."},
        {"50080", "XmlLocation is only allowed on properties of type org.xml.sax.Locator, but [{0}] is of type [{1}]."},
        {"50081", "An exception occurred during schema generation."},
        {"50082", "An attempt was made to write a value {0} without a key specified.  Try setting JSON_VALUE_WRAPPER on the JAXBMarshaller"},
        {"50083", "An error occurred while trying to instantiate the AccessorFactory class {0}"},
        {"50084", "The specified AccessorFactory class: {0} is invalid. It must implement createFieldAccessor(Class, Field, boolean) and createPropertyAccessor(Class, Method, Method)."},
        {"50085", "An exception occurred while invoking the createFieldAccessor method on the AccessorFactory {0}"},
        {"50086", "An exception occurred while invoking the createPropertyAccessor method on the AccessorFactory {0}"},
        {"50087", "An exception occurred while attempting to invoke the {0} method on the Accessor {1}"},
        {"50088", "Enum value {0} is not valid for an XmlEnum with class {1}"},
        {"50089", "The java interface {0} cannot be mapped by JAXB as it has multiple mappable parent interfaces. Multiple inheritence is not supported"},
        {"50090", "Invalid value for object graph: {0}. The value must be a string or an instance of ObjectGraph."},
        {"50091", "The element name {0} has more than one mapping."},
        {"50092", "Only one XmlElementRef property of type {0} allowed on class {1}."},
        {"50093", "The class {0} is not a mapped type in the JAXBContext."},
        {"50094", "The property {0} specified on the XmlVariableNode annotation was not found on the class {1}."},
        {"50095", "The property {0} of type {1} on the class {2} is not valid for a XmlVariableNode.  Only properties of type String or QName are allowed."},
        {"50096", "The @XmlAttribute property {0} in type {1} must reference a type that maps to text in XML.  {2} cannot be mapped to a text value."}
    };

    /**
     * Default constructor.
     */
    public JAXBExceptionResource() {
        // for reflection
    }

    /**
     * Return the lookup table.
     */
    @Override
    protected Object[][] getContents() {
        return contents;
    }

}
