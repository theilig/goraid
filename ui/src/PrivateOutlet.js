import { useAuth } from "../context/auth";

export default function PrivateOutlet() {

    return account ? (
        <HomeModule>
            <Outlet />
        </HomeModule>
    ) : (
        <Navigate to="/login" replace />
    );
}
