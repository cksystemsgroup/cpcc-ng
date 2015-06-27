package cpcc.rv.base.services;

import java.util.List;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;

import com.owlike.genson.annotation.JsonProperty;

/**
 * ConfigSyncData
 */
public class ConfigSyncData
{
    private List<SensorDefinition> sen;
    private List<RealVehicle> rvs;

    /**
     * @param sen the list of sensor definitions.
     * @param rvs the list of real vehicles.
     */
    public ConfigSyncData(@JsonProperty("sen") List<SensorDefinition> sen
        , @JsonProperty("rvs") List<RealVehicle> rvs)
    {
        this.sen = sen;
        this.rvs = rvs;
    }

    /**
     * @return the sensor definition list.
     */
    public List<SensorDefinition> getSen()
    {
        return sen;
    }

    /**
     * @param sen the sensor definition list to set
     */
    public void setSen(List<SensorDefinition> sen)
    {
        this.sen = sen;
    }

    /**
     * @return the the real vehicle list
     */
    public List<RealVehicle> getRvs()
    {
        return rvs;
    }

    /**
     * @param rvs the real vehicle list to set.
     */
    public void setRvs(List<RealVehicle> rvs)
    {
        this.rvs = rvs;
    }

}
