import logo from './logo.svg';
import './App.css';
import {
  BrowserRouter as Router,
  Route
} from 'react-router-dom'

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <Router>
          <div style={{ margin: 10 }}>
            <p>todo create component</p>
          </div>
          <div style={{ margin: 10 }}>
            <p>todo read component</p>
          </div>
          <div style={{ margin: 10 }}>
            <p>todo update component</p>
          </div>
          <div style={{ margin: 10 }}>
            <p>todo delete component</p>
          </div>
        </Router>
      </header>
    </div>
  );
}

export default App;
