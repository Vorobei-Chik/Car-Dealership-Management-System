package study.project.dealership.contracts.request;

import study.project.dealership.contracts.request.request.*;
import study.project.dealership.domain.request.RequestTestDrive;

import java.util.List;

public interface RequestService {
    RequestTestDrive createRequest(RequestCreateRequest request);

    RequestTestDrive findRequest(RequestFindRequest request);

    List<RequestTestDrive> getAllRequests();

    void removeRequest(RequestRemoveRequest request);

    RequestTestDrive updateRequest(RequestUpdateRequest request);
}
