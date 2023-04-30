import RegisterAndLoginForm from "./RegisterAndLoginForm.jsx";
import {useContext} from "react";
import {UserContext} from "./UserContext.jsx";

export default function Routes() {
    const {username, id} = useContext(UserContext)
    const token = localStorage.getItem("jwtToken");

    if (username) return "Logged in" + username

    return (
        <RegisterAndLoginForm />
    )

}
