import {
  BrowserRouter,
  Routes,
  Route,
  Navigate
} from 'react-router-dom';

import Layout from './components/Layout';
import ProtectedRoute from './components/ProtectedRoute';

import Login from './pages/Login';
import Register from './pages/Register';
import VerifyOtp from './pages/VerifyOtp';
import Home from './pages/Home';
import CreateRti from './pages/CreateRti';
import ReviewRti from './pages/ReviewRti';
import Requests from './pages/Requests';
import Sent from './pages/Sent';
import ForgotPassword from './pages/ForgotPassword';
import ResetPassword from './pages/ResetPassword';

export default function App() {

  return (

    <BrowserRouter>

      <Layout>

        <Routes>

          <Route
            path="/"
            element={
              <Navigate
                to={
                  localStorage.getItem('rti_token')
                    ? '/home'
                    : '/login'
                }
                replace
              />
            }
          />

          <Route
            path="/login"
            element={<Login />}
          />

          <Route
            path="/register"
            element={<Register />}
          />

          <Route
            path="/verify-otp"
            element={<VerifyOtp />}
          />

          <Route
            path="/forgot-password"
            element={<ForgotPassword />}
          />

          <Route
            path="/reset-password"
            element={<ResetPassword />}
          />


          <Route element={<ProtectedRoute />}>

            <Route
              path="/home"
              element={<Home />}
            />

            <Route
              path="/create"
              element={<CreateRti />}
            />

            <Route
              path="/review/:id"
              element={<ReviewRti />}
            />

            <Route
              path="/requests"
              element={<Requests />}
            />

            <Route
              path="/sent"
              element={<Sent />}
            />

          </Route>

        </Routes>

      </Layout>

    </BrowserRouter>
  );
}