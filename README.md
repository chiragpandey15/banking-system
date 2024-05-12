**Important: Don't forget to update the [Candidate README](#candidate-readme) section**

Real-time Transaction Challenge
===============================
## Overview
Welcome to Current's take-home technical assessment for backend engineers! We appreciate you taking the time to complete this, and we're excited to see what you come up with.

Today, you will be building a small but critical component of Current's core banking enging: real-time balance calculation through [event-sourcing](https://martinfowler.com/eaaDev/EventSourcing.html).

## Schema
The [included service.yml](service.yml) is the OpenAPI 3.0 schema to a service we would like you to create and host. 

## Details
The service accepts two types of transactions:
1) Loads: Add money to a user (credit)

2) Authorizations: Conditionally remove money from a user (debit)

Every load or authorization PUT should return the updated balance following the transaction. Authorization declines should be saved, even if they do not impact balance calculation.

You may use any technologies to support the service. We do not expect you to use a persistent store (you can you in-memory object), but you can if you want. We should be able to bootstrap your project locally to test.

## Expectations
We are looking for attention in the following areas:
1) Do you accept all requests supported by the schema, in the format described?

2) Do your responses conform to the prescribed schema?

3) Does the authorizations endpoint work as documented in the schema?

4) Do you have unit and integrations test on the functionality?

# Candidate README
## Bootstrap instructions
To run this server locally, do the following:

1. Download and install [maven](https://maven.apache.org/download.cgi)
2. Navigate to the project directory.
3. run the following commands  
  - mvn clean install  
  - mvn spring-boot:run


## Design considerations

### Class Design  
1. Error Class — To maintain specific schema
2. Amount Class — To maintain specific schema 
  - class methods AmountToFloat(),setAmountFloat(), debit(), and credit() is created for conveniency in calculation
3. TransactionRequest Class — As request for both the service (load and authorization) has same schema
4. Authorization Class — To maintain specific schema 
5. LoadResponse Class – As Load response schema is different from Authorization response

### Others
1. HashMap userBalance and userTransaction is created to maintain current balance of the user and transactions of users respectively. This makes calculation much convenient.
  - userTransaction keeps track of each transaction of different users. It is divided based on user so that in future we can use this to show transactions/ statement of perticular user to their profile.

## Bonus: Deployment considerations

If I were to deploy this component, I would keep the load service and authorization service separate from each other. I would containerize each service using Docker or other tools and host them on Kubernetes. This approach allows for scalability and flexibility, as each service can be scaled independently based on its specific traffic requirements. 

For database, we can use any database that follows the ACID properties.

## ASCII art
*Optional but suggested, replace this:*
```
                                                                                
                   _____________                                               
               ————————————————————                                            
             _________________________                                         
          ———————————————————————                                  ____        
        ____________________      _____                        ————————————      
     ———————————————————    ——————————————                 _________________   
   ___________________   ____________________           ————————————————————— 
 ——————————————————    ————————————————————————   —————————————————————————— 
    _____________               __________________    ————————————————————    
      —————————                     ————————————————    ————————————————       
         ————                          _________________     ————————                
                                          ——————————————————————————         
                                            ______________________            
                                               —————————————————               
                                                    —————————                    
```
## License

At CodeScreen, we strongly value the integrity and privacy of our assessments. As a result, this repository is under exclusive copyright, which means you **do not** have permission to share your solution to this test publicly (i.e., inside a public GitHub/GitLab repo, on Reddit, etc.). <br>

## Submitting your solution

Please push your changes to the `main branch` of this repository. You can push one or more commits. <br>

Once you are finished with the task, please click the `Submit Solution` link on <a href="https://app.codescreen.com/candidate/98bbbe6b-4791-4cc8-b379-2901b258291a" target="_blank">this screen</a>.