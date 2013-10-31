import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wolfram.alpha.*;

public class Swallow {
    private static Logger logger = LoggerFactory.getLogger(Swallow.class);

    // TODO add your own API key here
    private static String appid = "APIKEY";

    public int determineSpeedMph() throws WAException {
        String input = "What is the average airspeed velocity of an unladen swallow?";

        WAEngine engine = new WAEngine();
        engine.setAppID(appid);
        engine.addFormat("plaintext");

        // Create the query.
        WAQuery query = engine.createQuery();
        query.setInput(input);

        try {
            WAQueryResult queryResult = engine.performQuery(query);
            if (queryResult.isError()) {
                logger.error("Query error. Error code: {} error message: {}", queryResult.getErrorCode(), queryResult.getErrorMessage());
            } else if (!queryResult.isSuccess()) {
                logger.error("Query was not understood; no results available.");
            } else {
                // Got a result.
                logger.debug("Successful query. Pods follow:\n");
                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError()) {
                        logger.debug("Title={}", pod.getTitle());
                        for (WASubpod subpod : pod.getSubpods()) {
                            for (Object element : subpod.getContents()) {
                                if (element instanceof WAPlainText) {
                                    String s = ((WAPlainText) element).getText();
                                    logger.debug(s);
                                    int idx = s.indexOf("mph");
                                    if (idx >= 0) {
                                        return Integer.parseInt(s.substring(0, idx - 1));
                                    }
                                }
                            }
                        }
                    }
                }
                // We ignored many other types of Wolfram|Alpha output, such as warnings, assumptions, etc.
                // These can be obtained by methods of WAQueryResult or objects deeper in the hierarchy.
            }
        } catch (WAException e) {
            logger.error("An exception occurred", e);
            throw e;
        }
        throw new WAException("Could not find speed in result");
    }
}
