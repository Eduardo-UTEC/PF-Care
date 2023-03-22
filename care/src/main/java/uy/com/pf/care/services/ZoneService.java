package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.ZoneSaveException;
import uy.com.pf.care.model.documents.Zone;
import uy.com.pf.care.model.objects.NeighborhoodObject;
import uy.com.pf.care.repos.IZoneRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
@Log
public class ZoneService implements IZoneService{

    @Autowired
    private IZoneRepo zoneRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    //private static final Logger log = LoggerFactory.getLogger(CuidadosApplication.class);

    @Override
    public Zone save(Zone zone) {
        try{
            Zone newZone = zoneRepo.save(zone);
            log.info("*** Zona guardada con exito: " + LocalDateTime.now());
            return newZone;

        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO ZONA: " + e);
            throw new ZoneSaveException(zone);
        }
    }

    @Override
    public List<Zone> findAllZones(Boolean includeDeleted, String countryName) {
        if (includeDeleted)
            return zoneRepo.findByCountryName(countryName);
        else
            return zoneRepo.findByCountryNameAndDeletedFalse(countryName);
    }

    @Override
    public Optional<Zone> findId(String id) {
        return zoneRepo.findById(id);
    }

    // En este caso devuelvo el id de zona por cada barrio en el objeto NeighborhoodObject.
    // queda por determinar si se devuelve asi o solo un String con el nombre del barrio
    @Override
    public List<NeighborhoodObject> findAllNeighborhoods(
            Boolean includeDeleted, String cityName, String departmentName, String countryName) {

        List<NeighborhoodObject> list = new ArrayList<>();
        mongoTemplate.query(Zone.class).stream().forEach(zone -> {
                    if(zone.getCityName().equals(cityName) &&
                       zone.getDepartmentName().equals(departmentName) &&
                       zone.getCountryName().equals(countryName))

                        if (includeDeleted || ! zone.getDeleted())
                            list.add(new NeighborhoodObject(zone.getZone_id(), zone.getNeighborhoodName()));
        });

        return list.stream().sorted(new Comparator<NeighborhoodObject>() {
            @Override
            public int compare(NeighborhoodObject o1, NeighborhoodObject o2) {
                return o1.getNeighborhoodName().compareTo(o2.getNeighborhoodName());
            }
        }).toList();
    }

    @Override
    public List<String> findAllCities(Boolean includeDeleted, String departmentName, String countryName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("departmentName").is(departmentName).and("countryName").is(countryName));
        if (! includeDeleted)
            query.addCriteria(Criteria.where("deleted").is(false));
        return mongoTemplate.query(Zone.class)
                .distinct("cityName")
                .matching(query)
                .as(String.class)
                .all()
                .stream().sorted().toList();
    }

    @Override
    public List<String> findAllDepartments(Boolean includeDeleted, String countryName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("countryName").is(countryName));
        if (! includeDeleted)
            query.addCriteria(Criteria.where("deleted").is(false));
        return mongoTemplate.query(Zone.class)
                .distinct("departmentName")
                .matching(query)
                .as(String.class)
                .all()
                .stream().sorted().toList();
    }

    @Override
    public List<String> findAllCountries(Boolean includeDeleted) {
        Query query = new Query();
        query.addCriteria(Criteria.where("countryName").not().isNullValue());
        if (! includeDeleted)
            query.addCriteria(Criteria.where("deleted").is(false));
        return mongoTemplate.query(Zone.class)
                .distinct("countryName")
                .matching(query)
                .as(String.class)
                .all()
                .stream().sorted().toList();
    }

}
