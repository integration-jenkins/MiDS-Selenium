import React, { useState } from 'react';

const Form = () => {
    const [details, setDetails] = useState({ field1: '', field2: '' });

    const handleChange = (e) => {
        setDetails({ ...details, [e.target.name]: e.target.value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Testing Form :', details);
    };

    return (
        <form onSubmit={handleSubmit}>
            <label>
                Mai:
                <input type="text" name="field1" value={details.field1} onChange={handleChange} />
            </label>
            <br />
            <label>
                Tu:
                <input type="text" name="field2" value={details.field2} onChange={handleChange} />
            </label>
            <br />
            <button type="submit">Submit</button>
        </form>
    );
};

export default Form;