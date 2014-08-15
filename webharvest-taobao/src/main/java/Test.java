import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.log4j.PropertyConfigurator;
import org.webharvest.Harvest;
import org.webharvest.HarvestLoadCallback;
import org.webharvest.Harvester;
import org.webharvest.definition.ConfigSource;
import org.webharvest.definition.ConfigSourceFactory;
import org.webharvest.definition.IElementDef;
import org.webharvest.ioc.HttpModule;
import org.webharvest.ioc.ScraperModule;
import org.webharvest.runtime.DynamicScopeContext;
import org.webharvest.runtime.database.DefaultDriverManager;
import org.webharvest.runtime.database.DriverManager;
import org.webharvest.runtime.variables.ScriptingVariable;
import org.webharvest.runtime.web.HttpClientManager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class Test {

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        props.setProperty("log4j.rootLogger", "INFO, stdout");
        props.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        props.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        props.setProperty("log4j.appender.stdout.layout.ConversionPattern", "%-5p (%20F:%-3L) - %m\n");
        props.setProperty("log4j.logger.org.apache.http","DEBUG");
        PropertyConfigurator.configure(props);

        String workingDir = "webharvest-taobao/work";
        final Injector injector = Guice.createInjector(
                new ScraperModule(workingDir),
                new HttpModule(HttpClientManager.ProxySettings.NO_PROXY_SET));

//        final Logger logger = LogManager.getLogger(DebugFileLogger.NAME);
//        logger.setLevel(Level.TRACE);
//        logger.addAppender(new FileAppender(
//                new PatternLayout(DebugFileLogger.LAYOUT),
//                new File(workingDir, "_DEBUG").getPath(),
//                false));

        final DriverManager driverManager = DefaultDriverManager.INSTANCE;
        driverManager.addDriverResource(new File("webharvest-taobao/driver/mysql-connector-java-5.1.27.jar").toURI());

        final ConfigSourceFactory configSourceFactory =
                injector.getInstance(ConfigSourceFactory.class);
        final Harvest harvest = injector.getInstance(Harvest.class);
        final ConfigSource configSource = configSourceFactory.create(new File("webharvest-taobao/config/shop.xml"));
        Harvester harvester = harvest.getHarvester(configSource, new HarvestLoadCallback() {
            @Override
            public void onSuccess(List<IElementDef> elements) {

            }
        });

        harvester.execute(new Harvester.ContextInitCallback() {
            @Override
            public void onSuccess(DynamicScopeContext context) {
                context.setLocalVar("brand", "鲨科");
                context.setLocalVar("houbo",new ScriptingVariable(new HouboUtilities(context)));
            }
        });

//        ScraperConfiguration config = new ScraperConfiguration("c:/temp/scrapertest/configs/test2.xml");
//        ScraperConfiguration config = new ScraperConfiguration("spider.next/taobao/product.xml");
////        ScraperConfiguration config = new ScraperConfiguration( new URL("http://localhost/scripts/test/sample8.xml") );
//        Scraper scraper = new Scraper(config, "spider.next/work");
//        scraper.addVariableToContext("brand","鲨科");
//
//        scraper.setDebug(true);
//
//        long startTime = System.currentTimeMillis();
//        scraper.execute();
//        System.out.println("time elapsed: " + (System.currentTimeMillis() - startTime));

//        ScraperConfiguration config = new ScraperConfiguration("spider.next/taobao/shop.xml");
////        ScraperConfiguration config = new ScraperConfiguration( new URL("http://localhost/scripts/test/sample8.xml") );
//        Scraper scraper = new Scraper(config, "spider.next/work");
//        scraper.addVariableToContext("brand","鲨科");
//
//        scraper.setDebug(true);
//
//        long startTime = System.currentTimeMillis();
//        scraper.execute();
//        System.out.println("time elapsed: " + (System.currentTimeMillis() - startTime));
//    }
    }

}