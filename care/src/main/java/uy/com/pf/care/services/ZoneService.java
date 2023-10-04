package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.ZoneDuplicateKeyException;
import uy.com.pf.care.exceptions.ZoneSaveException;
import uy.com.pf.care.exceptions.ZoneUpdateException;
import uy.com.pf.care.infra.repos.IZoneRepo;
import uy.com.pf.care.model.documents.Zone;
import uy.com.pf.care.model.objects.NeighborhoodObject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class ZoneService implements IZoneService{

    @Autowired
    private IZoneRepo zoneRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    //private static final Logger log = LoggerFactory.getLogger(CuidadosApplication.class);

    @Override
    public String save(Zone zone) {
        try {
            this.defaultValues(zone);
            String id = zoneRepo.save(zone).getZoneId();
            log.info("*** Zona guardada con exito: " + LocalDateTime.now());
            return id;

        }catch(DuplicateKeyException e){
            String msg = "Error guardando zona (clave duplicada)";
            log.warning(msg + ": " + e.getMessage());
            throw new ZoneDuplicateKeyException(msg);
        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO ZONA: " + e);
            throw new ZoneSaveException("*** ERROR GUARDANDO ZONA");
        }
    }

    @Override
    public Boolean update(Zone newZone) {
        try{
            Optional<Zone> entityFound = zoneRepo.findById(newZone.getZoneId());
            if (entityFound.isPresent()){
                this.defaultValues(newZone, entityFound.get());
                zoneRepo.save(newZone);
                log.info("Zona actualizada con exito");
                return true;
            }
            log.info("No se encontro la zona con id " + newZone.getZoneId());
            return false;

        }catch(DuplicateKeyException e){
            String msg = "Error actualizando zona (clave duplicada)";
            log.warning(msg + ": " + e.getMessage());
            throw new ZoneDuplicateKeyException(msg);
        }catch(Exception e){
            log.warning("*** ERROR  ZONA: " + e);
            throw new ZoneUpdateException("*** ERROR ACTUALIZANDO ZONA");
        }

    }

    @Override
    public List<Zone> findAll(Boolean includeDeleted, String countryName) {
        if (includeDeleted)
            return zoneRepo.findByCountryName(countryName);

        return zoneRepo.findByCountryNameAndDeletedFalse(countryName);
    }

    @Override
    public Optional<Zone> findId(String id) {
        return zoneRepo.findById(id);
    }
/*
    // Se devuelve el id de zona por cada barrio en el objeto NeighborhoodObject.
    @Override
    public List<NeighborhoodObject> findNeighborhoods(
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
    }*/
    @Override
    public List<NeighborhoodObject> findNeighborhoods(
            Boolean includeDeleted, String cityName, String departmentName, String countryName) {

        Query query = new Query(Criteria.where("cityName").is(cityName)
                .and("departmentName").is(departmentName)
                .and("countryName").is(countryName));

        /*String cityNameRegex = "^" + Pattern.quote(cityName) + "$";
        String departmentNameRegex = "^" + Pattern.quote(departmentName) + "$";
        String countryNameRegex = "^" + Pattern.quote(countryName) + "$";

        Query query = new Query(Criteria.where("cityName").regex(cityNameRegex, "i")
                .and("departmentName").regex(departmentNameRegex, "i")
                .and("countryName").regex(countryNameRegex, "i"));
        */
        if (! includeDeleted)
            query.addCriteria(Criteria.where("deleted").is(false));

        return mongoTemplate.find(query, NeighborhoodObject.class, "Zones");
    }

    @Override
    public List<String> findCities(Boolean includeDeleted, String departmentName, String countryName) {
        Query query = new Query(Criteria.where("departmentName").is(departmentName).
                and("countryName").is(countryName));

        /*String departmentNameRegex = "^" + Pattern.quote(departmentName) + "$";
        String countryNameRegex = "^" + Pattern.quote(countryName) + "$";

        Query query = new Query(Criteria.where("departmentName").regex(departmentNameRegex, "i").
                and("countryName").regex(countryNameRegex, "i"));
        */
        if (! includeDeleted)
            query.addCriteria(Criteria.where("deleted").is(false));

        return mongoTemplate.findDistinct(query, "cityName", Zone.class, String.class);
    }

    @Override
    public List<String> findDepartments(Boolean includeDeleted, String countryName) {
        Query query = new Query(Criteria.where("countryName").is(countryName));

        //String countryNameRegex = "^" + Pattern.quote(countryName) + "$";
        //Query query = new Query(Criteria.where("countryName").regex(countryNameRegex, "i"));

        if (! includeDeleted)
            query.addCriteria(Criteria.where("deleted").is(false));

        return mongoTemplate.findDistinct(query, "departmentName", Zone.class, String.class);
    }

    @Override
    public List<String> findCountries(Boolean includeDeleted) {
        Query query = new Query(Criteria.where("countryName").not().isNullValue());
        if (! includeDeleted)
            query.addCriteria(Criteria.where("deleted").is(false));

        return mongoTemplate.findDistinct(query, "countryName", Zone.class, String.class);
    }

    @Override
    public Boolean setDeletion(String id, Boolean isDeleted) {
        Optional<Zone> zone = this.findId(id);
        if (zone.isPresent()) {
            zone.get().setDeleted(isDeleted);
            zoneRepo.save(zone.get());
            return true;
        }
        return false;
    }

    // Asigna los valores por default a la entitdad
    private void defaultValues(Zone zone){
        zone.setDeleted(false);
    }

    // Asigna los valores a la nueva entitdad, tomados de la vieja entidad (de la persistida)
    private void defaultValues(Zone newZone, Zone oldZone){
        newZone.setDeleted(oldZone.getDeleted());
    }
}
