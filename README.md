![curatolist_banner](https://github.com/user-attachments/assets/7685bbe3-8ed7-4765-a4f3-18efe87061c9)


![API Version 1](https://img.shields.io/badge/API-v1.0-blue?style=flat-square)

***

![Java Badge](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven Badge](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![Spring Badge](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

***

# Curatolist Api - Spring Boot Project

> [!NOTE]  
> This repository, and any of the files contained within, is part of a larger project.
>
> This API is made in tandem with an Android frontend, providing the data and services upon which it relies.
> 
> The Curatolist Android application exists in a separate repository which you can find [here](https://github.com/Jackaria72/Curatolist-AndroidApp).

## Set Up

In order to use this API, an API key is required from Harvard Art Museum, more details on how to obtain one can be found [here](https://github.com/harvardartmuseums/api-docs#access-to-the-api).
You will also need:
- An IDE (I reccomend IntelliJ)
- Java 21

Once you have obtained your key, you will need to make a fork of this project and clone it down to your local machine. Open the there insert your API Key into the designated space within the application.properties file (Found under main > resources), and run the project!

## Usage

### Basic Request

Once the API is running, you can begin to browse the the collection of Artwork!
By defeault the API will run on port 8080 of your local machine. In order to retrieve your results in a consise and readable format, I reccomend using [Postman](https://www.postman.com/)
Send your request to:
```
/curatolist/api/v1/art
```
> This will be proceeded by "http://localhost:8080" if you haven't made any changes to the project configuration.

Once you have done this you should recieve a nice chunk of artwork in the following format:
```
{
    "pageInfo": {
        "chicagoTotal": int,
        "chicagoPageTotal": int,
        "harvardTotal": int,
        "HarvardPageTotal": int,
        "combinedTotal": int,
        "combinedPageTotal": int
    },
    "artwork": [
        {
            "id": String,
            "title": String,
            "artist": String,
            "date": String,
            "description": String,
            "medium": String,
            "technique": String,
            "classification": String,
            "culturalOrigin": String,
            "dimensions": String,
            "imageUrl": String,
            "source": String
        },
        { ...more Artwork
        }
    ]
}
```

### Filtering

The following Query Parameters are avaliable in order to retrieve more specific results. These are:
- Search -> an expansive search powered by the brilliant Elastisearch querying avaliable from our client API's
- Source -> allowing you to filter the source of the Artwork between our two client API's
- Sort -> allowing you to order the incoming results by certain perameters
- Filter -> allowing you to filter results by certain categories
- Page -> allowing you to fetch the next page of results

#### Search
In order to use the search query please add the folling to your endpoint:
```
?q=<YOUR SEARCH HERE>
```
This is a powerful search function that will return results that match your term across multiple fields.

#### Source
In order to use the source Query please add the folling to you endpoint:
```
?source=<SOURCE>
```
The available source options are:
- "both"
- "harvard"
- "chicago"

Nothing added will return results for both.

#### Sort
In order to use the sort query please add the folling to your endpoint:
```
?sort=<SORT_TYPE>
```
The available sort operations are:
- Id
- Artwork Title
- Artwork Classification

#### Filter
In order to use the Filer endpoint please add the following to your endpoint:
```
?filter=<CHOSEN_FILTER_TYPE:VALUE_TO_FILTER_BY>
```
The available Filter types are:
- Artwork Classification
- Technique
- Medium

#### Page
In order to fetch subsequent pages of artwork please add the follwing to your endpoint:
```
?page=<PAGE_NUMBER>
```
> NOTE: These endpoints can be combined by replacing '?' with '&' for any queries after the first.


##### Acknowledgments 
The creator of Curatolist would like to thank everybody at both Harvard Art Museum and Chicago Institute of Art!
Without their hard work and dedication to making these beautiful works of art easily accessible to the public!

Please consider Visiting them and seeing all the great work they do!
