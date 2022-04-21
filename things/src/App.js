import logo from './logo.svg';
import './App.css';
import {
  BrowserRouter as Router,
  Route
} from 'react-router-dom'

import Create from './components/create'
import Read from './components/read'

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <Router>
          <div style={{ margin: 10 }} className="main">
            <h2 className="main-header">create</h2>
            <Route exact path='/create' component={Create} />
          </div>
          <div style={{ margin: 10 }}>
            <h2>list of things</h2>
            <Route exact path='/read' component={Read} />
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
