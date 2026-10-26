package appliedbotanicalmachinery.integration.igtooltip;

import appliedbotanicalmachinery.block.FluixPool;
import appliedbotanicalmachinery.block.FluixPoolBlockEntity;

import appeng.api.integrations.igtooltip.BaseClassRegistration;
import appeng.api.integrations.igtooltip.TooltipProvider;

@SuppressWarnings("UnstableApiUsage")
public class ABTooltipProvider implements TooltipProvider {

    @Override
    public void registerBlockEntityBaseClasses(BaseClassRegistration registration) {
        registration.addBaseBlockEntity(FluixPoolBlockEntity.class, FluixPool.class);
    }
}
