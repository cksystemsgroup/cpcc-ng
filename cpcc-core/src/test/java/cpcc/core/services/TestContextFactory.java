package cpcc.core.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/**
 * Context factory for unit tests.
 */
public class TestContextFactory implements InitialContextFactory
{
    public static final TestContext initialContext = new TestContext("");

    /**
     * Constructor.
     */
    public TestContextFactory()
    {
        System.out.println("TestContextFactory.<init>");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException
    {
        System.out.println("TestContextFactory.getInitialContext " + environment);
        return initialContext;
    }

    private static class TestContext implements Context
    {
        private Map<String, Context> subContextMap = new HashMap<>();

        // private Context self = null;
        // private Object bound = null;
        // private String name;

        public TestContext(String name)
        {
            //            this.name = name;
        }

        @Override
        public Object lookup(Name name) throws NamingException
        {
            System.out.println("MyInitialContext.lookup() Name=" + name);
            return null;
        }

        @Override
        public Object lookup(String name) throws NamingException
        {
            System.out.println("MyInitialContext.lookup() name=" + name);
            return null;
        }

        @Override
        public void bind(Name name, Object obj) throws NamingException
        {
            System.out.println("MyInitialContext.bind() Name=" + name + " obj=" + obj);

        }

        @Override
        public void bind(String name, Object obj) throws NamingException
        {
            System.out.println("MyInitialContext.bind() name=" + name + " obj=" + obj);

            // bound.put(name, obj);

            String[] ctxs = name.split("/");
            if (ctxs.length == 1)
            {
                // bound = obj;
            }
            else
            {
                subContextMap.get(ctxs[0]).bind(name.split("/", 2)[1], obj);
            }
        }

        @Override
        public void rebind(Name name, Object obj) throws NamingException
        {
            System.out.println("MyInitialContext.rebind() Name=" + name + " obj=" + obj);

        }

        @Override
        public void rebind(String name, Object obj) throws NamingException
        {
            System.out.println("MyInitialContext.rebind() name=" + name + " obj=" + obj);

        }

        @Override
        public void unbind(Name name) throws NamingException
        {
            System.out.println("MyInitialContext.unbind() Name=" + name);

        }

        @Override
        public void unbind(String name) throws NamingException
        {
            System.out.println("MyInitialContext.unbind() name=" + name);

        }

        @Override
        public void rename(Name oldName, Name newName) throws NamingException
        {
            System.out.println("MyInitialContext.rename() oldName=" + oldName + " newName=" + newName);

        }

        @Override
        public void rename(String oldName, String newName) throws NamingException
        {
            System.out.println("MyInitialContext.rename() oldname=" + oldName + " newname=" + newName);

        }

        @Override
        public NamingEnumeration<NameClassPair> list(Name name) throws NamingException
        {
            System.out.println("MyInitialContext.list() Name=" + name);
            return null;
        }

        @Override
        public NamingEnumeration<NameClassPair> list(String name) throws NamingException
        {
            System.out.println("MyInitialContext.list() name=" + name);
            return null;
        }

        @Override
        public NamingEnumeration<Binding> listBindings(Name name) throws NamingException
        {
            System.out.println("MyInitialContext.listBindings() Name=" + name);
            return null;
        }

        @Override
        public NamingEnumeration<Binding> listBindings(String name) throws NamingException
        {
            System.out.println("MyInitialContext.listBindings() name=" + name);
            return null;
        }

        @Override
        public void destroySubcontext(Name name) throws NamingException
        {
            System.out.println("MyInitialContext.destroySubcontext() Name=" + name);

        }

        @Override
        public void destroySubcontext(String name) throws NamingException
        {
            System.out.println("MyInitialContext.destroySubcontext() name=" + name);

        }

        @Override
        public Context createSubcontext(Name name) throws NamingException
        {
            System.out.println("MyInitialContext.createSubcontext() Name=" + name);
            return null;
        }

        @Override
        public Context createSubcontext(String name) throws NamingException
        {
            // http://grepcode.com/file/repo1.maven.org/maven2/org.springframework/spring-test/4.2.0.RELEASE/org/springframework/mock/jndi/SimpleNamingContext.java?av=f

            System.out.println("MyInitialContext.createSubcontext() name=" + name);

            try
            {
                URI uri = new URI(name);

                System.out.printf("scheme=%s, host=%s, path=%s%n", uri.getScheme(), uri.getHost(), uri.getPath());
            }
            catch (URISyntaxException e)
            {
                e.printStackTrace();
                throw new NamingException(e.getMessage());
            }

            return null;

            //            String[] ctxs = name.split("/");
            //            if (ctxs.length == 1)
            //            {
            //                if (self == null)
            //                {
            //                    self = new TestContext(name);
            //                }
            //                return self;
            //            }
            //            else
            //            {
            //                String subContextName = ctxs[1];
            //                if (!subContextMap.containsKey(subContextName))
            //                {
            //                    TestContext subContext = new TestContext(subContextName);
            //                    subContextMap.put(subContextName, subContext);
            //                    
            //                    
            //                    return subContext;
            //                }
            //
            //                if (ctxs.length > 2)
            //                {
            //                    subContextName = name.split("/", 3)[2];
            //                    Context subContext = subContextMap.get(ctxs[1]);
            //                    if (subContext == null){
            //                        subContext = new TestContext(ctxs[1]);
            //                        
            //                    }
            //                    subContext.createSubcontext(subContextName);
            //                }
            //
            //                return subContextMap.get(subContextName);
            //            }
        }

        @Override
        public Object lookupLink(Name name) throws NamingException
        {
            System.out.println("MyInitialContext.lookupLink() Name=" + name);
            return null;
        }

        @Override
        public Object lookupLink(String name) throws NamingException
        {
            System.out.println("MyInitialContext.lookupLink() name=" + name);
            return null;
        }

        @Override
        public NameParser getNameParser(Name name) throws NamingException
        {
            System.out.println("MyInitialContext.getNameParser() Name=" + name);
            return null;
        }

        @Override
        public NameParser getNameParser(String name) throws NamingException
        {
            System.out.println("MyInitialContext.getNameParser() name=" + name);
            return null;
        }

        @Override
        public Name composeName(Name name, Name prefix) throws NamingException
        {
            System.out.println("MyInitialContext.composeName() Name=" + name + " Prefix=" + prefix);
            return null;
        }

        @Override
        public String composeName(String name, String prefix) throws NamingException
        {
            System.out.println("MyInitialContext.composeName() name=" + name + " prefix=" + prefix);
            return null;
        }

        @Override
        public Object addToEnvironment(String propName, Object propVal) throws NamingException
        {
            System.out.println("MyInitialContext.addToEnvironment() propName=" + propName + " propVal=" + propVal);
            return null;
        }

        @Override
        public Object removeFromEnvironment(String propName) throws NamingException
        {
            System.out.println("MyInitialContext.removeFromEnvironment() propName=" + propName);
            return null;
        }

        @Override
        public Hashtable<?, ?> getEnvironment() throws NamingException
        {
            System.out.println("MyInitialContext.getEnvironment() ");
            return null;
        }

        @Override
        public void close() throws NamingException
        {
            System.out.println("MyInitialContext.close() ");

        }

        @Override
        public String getNameInNamespace() throws NamingException
        {
            System.out.println("MyInitialContext.getNameInNamespace() ");
            return null;
        }

    }
}
