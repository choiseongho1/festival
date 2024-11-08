import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import SignUp from './pages/SignUp';
import FestivalList from './components/FestivalList';
import FestivalDetail from './components/FestivalDetail';
import './App.css';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/festival" element={<FestivalList />} />
        <Route path="/festivals/:contentId" element={<FestivalDetail />} />
      </Routes>
    </Router>
  );
}

export default App;
