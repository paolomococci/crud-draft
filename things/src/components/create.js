import axios from 'axios'
import {
  React,
  useState
} from 'react'
import {
  Form,
  Button,
  Checkbox
} from 'semantic-ui-react'
import {
  useHistory
} from 'react-router'

function Create() {
  let history = useHistory()
  const [
    name,
    setName
  ] = useState('')
  const [
    surname,
    setSurname
  ] = useState('')
  const [
    checked,
    setChecked
  ] = useState(false)
  const postData = () => {
    axios.post(
      `http://localhost/sampledata`,
      {
        name,
        surname,
        checked
      }
    ).then(() => {
      history.push('/read')
    })
  }

  return (
    <div>
      <Form className="create-form">
      <Form.Field>
        <label>name</label>
        <input placeholder="name" onChange={(e) => setName(e.target.value)} />
      </Form.Field>
      <Form.Field>
        <label>surname</label>
        <input placeholder="surname" onChange={(e) => setSurname(e.target.value)} />
      </Form.Field>
      <Form.Field>
        <Checkbox label="accept terms and conditions" onChange={() => setChecked(!checked)} />
      </Form.Field>
      </Form>
    </div>
  )
}

export default Create
