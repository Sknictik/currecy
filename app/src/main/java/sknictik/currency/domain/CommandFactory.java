package sknictik.currency.domain;

import sknictik.currency.data.service.WebService;
import sknictik.currency.domain.command.ExchangeRateCommand;

public class CommandFactory {

    private WebService webService;

    public CommandFactory(WebService webService) {
        this.webService = webService;
    }

    public ExchangeRateCommand getExchangeRateCommand() {
        return new ExchangeRateCommand(webService);
    }

}
