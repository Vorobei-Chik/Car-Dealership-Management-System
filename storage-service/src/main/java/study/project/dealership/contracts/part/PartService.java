package study.project.dealership.contracts.part;

import study.project.dealership.contracts.part.request.*;
import study.project.dealership.domain.part.*;

import java.util.List;

public interface PartService {
    void linkPartToModel(RequestLinkPartToModel request);

    void unlinkPartFromModel(RequestUnlinkPartFromModel request);

    Engine createEngine(RequestCreateEngine request);

    List<Engine> getEnginesForModel(RequestGetEnginesForModel request);

    Engine updateEngine(RequestUpdateEngine request);

    GearBox createGearBox(RequestCreateGearBox request);

    List<GearBox> getGearBoxes(RequestGetGearBoxesForModel request);

    GearBox updateGearBox(RequestUpdateGearBox request);

    Interior createInterior(RequestCreateInterior request);

    List<Interior> getInteriors(RequestGetInteriorsForModel request);

    Interior updateInterior(RequestUpdateInterior request);

    Rudder createRudder(RequestCreateRudder request);

    List<Rudder> getRudders(RequestGetRuddersForModel request);

    Rudder updateRudder(RequestUpdateRudder request);

    Transmission createTransmission(RequestCreateTransmission request);

    List<Transmission> getTransmissions(RequestGetTransmissionsForModel request);

    Transmission updateTransmission(RequestUpdateTransmission request);

    Wheel createWheel(RequestCreateWheel request);

    List<Wheel> getWheels(RequestGetWheelsForModel request);

    Wheel updateWheel(RequestUpdateWheel request);

    Part findPart(RequestFindPart request);

    void removePart(RequestRemovePart request);

    Part updatePart(RequestUpdatePart request);
}
