import {UserContextProvider} from "./UserContext.jsx"
import axios from "axios";
import Routes from "./Routes.jsx";
function App() {

    axios.defaults.baseURL = "http://localhost:8080/api/v1";
    // to be able to send cookies to the server (credentials)
    axios.defaults.withCredentials = true

  return (
      <UserContextProvider>
          <Routes />
      </UserContextProvider>
  )
}

export default App
