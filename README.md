# StoicOpenAI Backend

## Overview
This backend serves as the server-side component of the StoicOpenAI web application, which integrates OpenAI's GPT-4 and DALL-E models to provide users with tailored Stoic quotes and visuals.

## Getting Started
To set up this project locally:

1. Clone the repository: `https://github.com/sadekkk12/StoicOpenAI_backend.git`
2. Install dependencies: `mvn clean install` (Make sure Maven is installed)
3. Set up environment variables for your OpenAI API key in `application.properties` or as system variables.
4. Run the server: `Run StoicOpenAiBackendApplication`

## API Documentation
The backend provides the following endpoints:
- `/quotes?userInput={userInput}`: Fetches five quotes based on the user's input.
- `/explanation?quote={quote}`: Provides an explanation for a given Stoic quote.
- `/generate-image`: Generates an AI-based visual representation of a given quote.

## Technology Stack
- Java with Spring Boot
- Maven for build automation
- OpenAI's GPT-4 and DALL-E models via RESTful APIs

## Deployment
This backend is deployed on Azure and uses GitHub Actions for CI/CD.

## Related Repositories
- https://github.com/sadekkk12/StoicOpenAI_frontend

